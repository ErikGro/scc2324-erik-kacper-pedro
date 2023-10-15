package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.data.house.AvailablePeriodDAO;
import scc.data.house.House;
import scc.data.house.HouseDAO;
import scc.db.CosmosDBLayer;

import java.util.UUID;

/**
 * Resource for managing media files, such as images.
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
		int statusCode = putHouse(UUID.randomUUID().toString(), house);

		return Response.status(statusCode).build();
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
		int statusCode = putHouse(id, house);

		return Response.status(statusCode).build();
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

	private int putHouse(String id, House house) {
		HouseDAO houseDAO = new HouseDAO(house);
		houseDAO.setId(id);
		CosmosItemResponse<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.putHouse(houseDAO);

		house.getAvailablePeriods().forEach(period -> {
			AvailablePeriodDAO dao = new AvailablePeriodDAO(period);
			dao.setHouseID(response.getItem().getId());
			dao.setId(UUID.randomUUID().toString());
			CosmosDBLayer.getInstance().availablePeriodDB.putAvailablePeriod(dao);
		});

		return response.getStatusCode();
	}
}
