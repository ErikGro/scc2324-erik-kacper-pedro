package scc.srv;

import com.azure.cosmos.util.CosmosPagedIterable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.cache.HouseService;
import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.house.HouseDAO;
import scc.db.CosmosDBLayer;
import scc.db.blob.BlobLayer;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Resource for accessing houses
 */ 
@Path("/house")
public class HouseResource {
	private final HouseService houseService = new HouseService();
	private final UserService userService = new UserService();

	/**
	 * Create a single house
	 * @param houseDAO the house to be created
	 * @return the id of the house
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postHouse(HouseDAO houseDAO) {
		houseDAO.setId(UUID.randomUUID().toString());

		ServiceResponse<HouseDAO> response = houseService.upsert(houseDAO);
		// TODO: add houseID to owner's houseIDs list

		if (response.getStatusCode() != 201 || response.getItem().isEmpty()) {
			return Response.status(response.getStatusCode()).build();
		}

		URI housePath = URI.create("/house/" + response.getItem().get().getId());
		return Response.created(housePath).build();
	}

	/**
	 * Update a house by a given id
	 * @param id the id of the house to be updated
	 * @param houseDAO the updated content
	 * @return nothing - 2xx if update was successful
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putHouse(@CookieParam("scc:session") Cookie session,
							 @PathParam("id") String id,
							 HouseDAO houseDAO) {
		ServiceResponse<HouseDAO> houseResponse = houseService.getByID(id);

		if (houseResponse.getItem().isEmpty())
			return Response.status(404).build();

		if (session == null || session.getValue() == null ||
				userService.userSessionInvalid(session.getValue(), houseResponse.getItem().get().getOwnerID()))
			return Response.status(401).build();

		houseDAO.setId(id);
		ServiceResponse<HouseDAO> response = houseService.upsert(houseDAO);

		return Response.status(response.getStatusCode()).build();
	}

	/**
	 * If house with given id exists, return house as JSON
	 * @param id of the house
	 * @return Response with house JSON for given id in body
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHouseByID(@PathParam("id") String id) {
		ServiceResponse<HouseDAO> response = houseService.getByID(id);

        if (response.getItem().isPresent()) {
			return Response.accepted(response.getItem().get()).build();
		} else {
			return Response.noContent().build();
		}
	}

	/**
	 * Delete a house by a given id
	 * @param id of the house to be deleted
	 * @return nothing - 2xx if delete succeeded
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteHouse(@CookieParam("scc:session") Cookie session,
								@PathParam("id") String id) {
		ServiceResponse<HouseDAO> response = houseService.getByID(id);

		if (response.getItem().isEmpty())
			return Response.status(404).build();

		if (session == null || session.getValue() == null ||
				userService.userSessionInvalid(session.getValue(), response.getItem().get().getOwnerID()))
			return Response.status(401).build();

		ServiceResponse<HouseDAO> deleteResponse = houseService.deleteByID(id);

		return Response.status(deleteResponse.getStatusCode()).build();
	}

	/**
	 * Returns all houses for the given query Parameter.
	 * Valid Query parameters are:
	 * - userID
	 * - city
	 * - city & start-date & end-date
	 * @param userID of the owner of the house
	 * @param city of the house
	 * @param startDate of the period
	 * @param endDate of the period
	 * @return all houses for the given query parameters
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHousesByQuery(@QueryParam("user-id") String userID,
									 @QueryParam("city") String city,
									 @QueryParam("start-date") String startDate,
									 @QueryParam("end-date") String endDate) {
		CosmosPagedIterable<HouseDAO> response;

		if (isValidQuery(userID)) { // List of houses of a given user
			response = CosmosDBLayer.getInstance().houseDB.getHousesByUserID(userID);
		} else if (isValidQuery(city) && isValidQuery(startDate) && isValidQuery(endDate)) { // Search of available houses for a given period and location
			response = CosmosDBLayer.getInstance().houseDB.getHousesByCityAndPeriod(city, startDate, endDate);
		} else if (isValidQuery(city)) { // List of available houses for a given location
			response = CosmosDBLayer.getInstance().houseDB.getHousesByCity(city);
		} else {
			return Response.status(400).build();
		}

		return Response.accepted(response.stream().toList()).build();
	}

	private boolean isValidQuery(String string) {
		return string != null && !string.trim().isEmpty();
	}

	/**
	 * Returns all houses which have soon a discount
	 * @return all houses which have soon a discount
	 */
	@GET
	@Path("/discounted-soon")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiscountedHousesNearFuture() {
		CosmosPagedIterable<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.getDiscountedHousesNearFuture();

		return Response.accepted(response.stream().toList()).build();
	}

	/////////////////// PHOTOS ENDPOINTS ///////////////////////
	@POST
	@Path("/{houseID}/photo")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadPhoto(@CookieParam("scc:session") Cookie session,
								@PathParam("houseID") String houseID, byte[] photo) {
		Optional<HouseDAO> optionalHouse = houseService.getByID(houseID).getItem();

        if (optionalHouse.isEmpty())
            return Response.status(404).entity("House doesn't exist.").build();

		HouseDAO house = optionalHouse.get();

		if (session == null || session.getValue() == null ||
				userService.userSessionInvalid(session.getValue(), house.getOwnerID()))
			return Response.status(401).build();

		BlobLayer blobLayer = BlobLayer.getInstance();

		String photoID = UUID.randomUUID().toString();
		blobLayer.housesContainer.uploadImage(photoID, photo);

		// Update house photoIDs list by new photoID		
		ArrayList<String> photoIDs = new ArrayList<>(house.getPhotoIDs());
		photoIDs.add(houseID);
		house.setPhotoIDs(photoIDs);

		houseService.upsert(house);

		return Response.ok(houseID).entity("Photo with id " + photoID + " uploaded to house with id " + houseID).build();
	}

	@GET
	@Path("/{houseID}/photo/{photoID}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getPhoto(@PathParam("houseID") String houseID, @PathParam("photoID") String photoID) {

        if (houseService.getByID(houseID).getItem().isEmpty()) {
			return Response.status(404).entity("House doesn't exist.").build();
		}
		
		BlobLayer blobLayer = BlobLayer.getInstance();

		byte[] photo;
		try {
			photo = blobLayer.housesContainer.getImage(photoID);
		} catch (Exception e) {
			return Response.status(404).entity("No photo found").build();
		}

		return Response.ok(photo).build();
	}

}
