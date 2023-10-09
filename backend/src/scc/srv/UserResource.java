package scc.srv;

import scc.db.*;

import java.util.Locale;
import java.util.Random;
import java.util.RandomAccess;

import com.azure.cosmos.implementation.apachecommons.math.random.RandomData;
import com.azure.cosmos.models.CosmosItemResponse;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import scc.data.User;
import scc.data.UserDAO;

/**
 * Class with control endpoints.
 */
@Path("/user")
public class UserResource
{

	/**
	 * This methods just prints a string. It may be useful to check if the current 
	 * version is running on Azure.
	 */

	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User data) {
		if(!data.isValid())
			return Response.status(400).entity("Incorrect registration data").build();
		
		Locale.setDefault(Locale.US);
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		String id = "0:" + System.currentTimeMillis();
		CosmosItemResponse<UserDAO> res = null;
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setPhotoId(data.getPhotoId());
		u.setHouseIds(data.getHouseIds());

		res = db.putUser(u);
		data.setId(id);
		return Response.ok(data.getId()).build();
	}
	
	public Boolean userExists(String id) {
		Locale.setDefault(Locale.US);
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		return  db.getUserById(id)!=null;
	}
	
	@Path("/{id}/delete")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("id") String id){
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		//TODO: add compatibility with houses to show that the user has been deleted
		db.delUserById(id);
		throw new ServiceUnavailableException();

	}
	//TODO: transactions
	@Path("/{id}/update")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") String id, User data){
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		
		
		throw new ServiceUnavailableException();
	}

}
