package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.data.house.AvailableMonthDAO;
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
		HouseDAO houseDAO = new HouseDAO(house);
		houseDAO.setId(UUID.randomUUID().toString());
		CosmosItemResponse<HouseDAO> response = CosmosDBLayer.getInstance().houseDB.putHouse(houseDAO);

		house.getAvailableMonths().forEach(availableMonth -> {
			AvailableMonthDAO dao = new AvailableMonthDAO(availableMonth);
			dao.setHouseID(response.getItem().getId());
			dao.setId(UUID.randomUUID().toString());
			CosmosDBLayer.getInstance().availableMonthDB.putAvailableMonth(dao);
		});

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
		throw new ServiceUnavailableException();
	}

	/**
	 * Delete a house by a given id
	 * @param id of the house to be deleted
	 * @return nothing - 2xx if delete succeeded
	 */
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		throw new ServiceUnavailableException();
	}
}
