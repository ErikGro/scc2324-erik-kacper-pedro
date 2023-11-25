package scc.srv;

import java.util.List;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.cache.HouseService;
import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.house.House;
import scc.data.house.HouseDAO;
import scc.persistence.db.cosmos.CosmosDBLayer;
import scc.persistence.media.FileSystemService;
import scc.persistence.media.MediaService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Resource for accessing houses
 */ 
@Path("/house")
public class HouseResource {
	private final HouseService houseService = new HouseService();
	private final UserService userService = new UserService();
	private final MediaService mediaService = FileSystemService.getInstance();

	/**
	 * Create a single house
	 * @param houseDAO the house to be created
	 * @return the id of the house
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postHouse(@CookieParam("scc:session") Cookie session,
							  HouseDAO houseDAO) {
		if (session == null || session.getValue() == null)
			return Response.status(401).build();

		Optional<String> userID = userService.getUserIDBySession(session.getValue());

		if (userID.isEmpty())
			return Response.status(401).build();

		houseDAO.setOwnerID(userID.get());
		houseDAO.setId(UUID.randomUUID().toString());

		ServiceResponse<HouseDAO> response = houseService.upsert(houseDAO);

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

		Optional<String> userID = userService.getUserIDBySession(session.getValue());

		if (userID.isEmpty())
			return Response.status(401).build();

		houseDAO.setOwnerID(userID.get());
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
			House house = new House(response.getItem().get());
			return Response.ok(house).build();
		} else {
			throw new NotFoundException("House with the given id does not exist");
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

		ServiceResponse<Object> deleteResponse = houseService.deleteByID(id);

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
		ServiceResponse<List<HouseDAO>> response;

		if (isValidQuery(userID)) { // List of houses of a given user
			response = CosmosDBLayer.getInstance().getHouseContainer().getHousesByUserID(userID);
		} else if (isValidQuery(city) && isValidQuery(startDate) && isValidQuery(endDate)) { // Search of available houses for a given period and location
			response = CosmosDBLayer.getInstance().getHouseContainer().getHousesByCityAndPeriod(city, startDate, endDate);
		} else if (isValidQuery(city)) { // List of available houses for a given location
			response = CosmosDBLayer.getInstance().getHouseContainer().getHousesByCity(city);
		} else {
			return Response.status(400).build();
		}

		if (response.getItem().isEmpty()) {
			throw new NotFoundException();
		}

		List<House> houses = response.getItem().get()
				.stream()
				.map(House::new)
				.collect(Collectors.toList());

		return Response.ok(houses).build();
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
		List<HouseDAO> discountedSoon = houseService.getDiscountedSoon();

		List<House> houses = discountedSoon
				.stream()
				.map(House::new)
				.toList();

		return Response.ok(houses).build();
	}

	/////////////////// PHOTOS ENDPOINTS ///////////////////////
	@POST
	@Path("/{houseID}/photo")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadPhoto(@CookieParam("scc:session") Cookie session,
								@PathParam("houseID") String houseID,
								byte[] photo) {
		Optional<HouseDAO> optionalHouse = houseService.getByID(houseID).getItem();

        if (optionalHouse.isEmpty())
			throw new NotFoundException("House doesn't exist.");

		HouseDAO house = optionalHouse.get();

		if (session == null || session.getValue() == null ||
				userService.userSessionInvalid(session.getValue(), house.getOwnerID()))
			throw new NotAuthorizedException("Not authorized.");

		String newPhotoID = UUID.randomUUID().toString();
		mediaService.getHousesContainer().upsertImage(newPhotoID, photo);

		ArrayList<String> photoIDs = new ArrayList<>(house.getPhotoIDs());
		photoIDs.add(newPhotoID);
		house.setPhotoIDs(photoIDs);
		houseService.upsert(house);

		return Response.ok().build();
	}

	@Path("/photo/{id}")
	@GET
	@Produces({"image/png", "image/jpeg"})
	public Response getPhoto(@PathParam("id") String id) {
		Optional<byte[]> byteArray = mediaService.getHousesContainer().getImageBytes(id);

		if (byteArray.isEmpty())
			throw new NotFoundException("Image not found.");

		return Response.ok(byteArray.get()).build();
	}
}
