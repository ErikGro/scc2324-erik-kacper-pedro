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
import java.util.UUID;

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
		CosmosItemResponse<HouseDAO> repsonse = putHouse(id, house);

		return Response.status(repsonse.getStatusCode()).build();
	}

	/**
	 * If house with given id exists, return house as JSON
	 * @param id
	 * @return Response with house JSON for given id in body
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHouseByID(@PathParam("id") String id) {
		CosmosItemResponse<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.getHouseByID(id);

		return Response.accepted(response.getItem()).build();
	}

	/**
	 * Returns all houses for a given query parameter userID
	 * @param userID
	 * @return all houses for a given query parameter userID
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHousesByUserID(@QueryParam("userID") String userID) {
		CosmosPagedIterable<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.getHousesByUserID(userID);

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

	private CosmosItemResponse<HouseDAO> putHouse(String id, House house) {
		HouseDAO houseDAO = new HouseDAO(house);
		houseDAO.setId(id);
		CosmosItemResponse<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.putHouse(houseDAO);

		house.getAvailablePeriods().forEach(period -> {
			AvailablePeriodDAO dao = new AvailablePeriodDAO(period);
			dao.setHouseID(response.getItem().getId());
			dao.setId(UUID.randomUUID().toString());
			CosmosDBLayer.getInstance().availablePeriodDB.putAvailablePeriod(dao);
		});

		return response;
	}
}
