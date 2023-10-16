package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.data.house.AvailablePeriodDAO;
import scc.data.house.House;
import scc.data.house.HouseDAO;
import scc.db.CosmosDBLayer;
import scc.utils.Constants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Resource for accessing houses
 */ 
@Path("/house")
public class HouseResource
{
	/**
	 * Create a single house
	 * @param house the house to be created
	 * @return the id of the house
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(House house) {
		CosmosItemResponse<HouseDAO> response = putHouse(UUID.randomUUID().toString(), house);

		if (response.getStatusCode() == 201) {
			try {
				String id = response.getItem().getId();

				URI houseURL = new URI(Constants.getApplicationURL() + "/rest/house/" + id);
				return Response.created(houseURL).build();
			} catch (URISyntaxException e) {
				return Response.status(500).build();
			}
        }

		return Response.status(response.getStatusCode()).build();
	}

	/**
	 * Update a house by a given id
	 * @param id the id of the house to be updated
	 * @param house the updated content
	 * @return nothing - 2xx if update was successful
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(@PathParam("id") String id, House house) {
		CosmosItemResponse<HouseDAO> response = putHouse(id, house);

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
		CosmosItemResponse<HouseDAO> responseHouse = CosmosDBLayer.getInstance().houseDB.getHouseByID(id);

		if (responseHouse.getStatusCode() < 300) {
			CosmosPagedIterable<AvailablePeriodDAO> responseAvailability = CosmosDBLayer.getInstance().availablePeriodDB.getAvailablePeriodsForHouse(id);
			Set<AvailablePeriodDAO> availablePeriods = responseAvailability.stream().collect(Collectors.toSet());
			House house = new House(responseHouse.getItem(), availablePeriods);

			return Response.accepted(house).build();
		} else {
			return Response.noContent().build();
		}
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
	public Response getHousesByQuery(@QueryParam("userID") String userID,
									 @QueryParam("city") String city,
									 @QueryParam("start-date") String startDate,
									 @QueryParam("end-date") String endDate) {
		CosmosPagedIterable<HouseDAO> response;

		if (userID != null) { // List of houses of a given user
			response = CosmosDBLayer.getInstance().houseDB.getHousesByUserID(userID);
		} else if (city != null && startDate != null && endDate != null) { // Search of available houses for a given period and location
			response = CosmosDBLayer.getInstance().houseDB.getHousesByCityAndPeriod(city, startDate, endDate);
		} else if (city != null) { // List of available houses for a given location
			response = CosmosDBLayer.getInstance().houseDB.getHousesByCity(city);
		} else {
			return Response.status(400).build();
		}

		return Response.accepted(response.stream().toList()).build();
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

	/**
	 * Returns all houses for a given city
	 * @param city the city to be queried
	 * @return all houses for a given query parameter city
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHousesByCity(@QueryParam("city") String city) {
		CosmosPagedIterable<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.getHousesByCity(city);

		return Response.accepted(response.stream().toList()).build();
	}

	/**
	 * Delete a house by a given id
	 * @param id of the house to be deleted
	 * @return nothing - 2xx if delete succeeded
	 */
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		CosmosItemResponse<Object> response = CosmosDBLayer.getInstance().houseDB.deleteHouse(id);
		return Response.status(response.getStatusCode()).build();
	}

	//////////////////////////////// Helper methods ////////////////////////////////
	private CosmosItemResponse<HouseDAO> putHouse(String id, House house) {
		HouseDAO houseDAO = new HouseDAO(house);
		houseDAO.setId(id);
		CosmosItemResponse<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.putHouse(houseDAO);

		String houseID = response.getItem().getId();
		CosmosDBLayer.getInstance().availablePeriodDB.deletePeriodsForHouse(houseID);

		house.getAvailablePeriods().forEach(period -> {
			AvailablePeriodDAO dao = new AvailablePeriodDAO(period);
			dao.setHouseID(houseID);
			dao.setId(UUID.randomUUID().toString());
			CosmosDBLayer.getInstance().availablePeriodDB.createAvailablePeriod(dao);
		});

		return response;
	}
}
