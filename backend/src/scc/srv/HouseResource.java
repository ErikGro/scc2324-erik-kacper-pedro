package scc.srv;

import com.azure.cosmos.models.CosmosItemResponse;
import jakarta.ws.rs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jakarta.ws.rs.core.MediaType;
import scc.data.House;
import scc.data.HouseDAO;
import scc.db.CosmosDBLayer;
import scc.db.HouseDB;

/**
 * Resource for managing media files, such as images.
 */ 
@Path("/house")
public class HouseResource
{
	HouseDB db;

	HouseResource() {
		db = CosmosDBLayer.getInstance().houseDB;
	}

	/**
	 * Create a single house
	 * @param house the house to be created
	 * @return the id of the house
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(House house) {
		HouseDAO dao = new HouseDAO(house.getName());
		CosmosItemResponse<HouseDAO> response = db.putHouse(dao);

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
