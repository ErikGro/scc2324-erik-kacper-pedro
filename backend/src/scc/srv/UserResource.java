package scc.srv;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
/*
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(RegisterUser data) {
		throw new ServiceUnavailableException();
	}

	@Path("/{id}/delete")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("id") String id){
		throw new ServiceUnavailableException();
	}
	
	@Path("/{id}/update")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") String id, RegisterUser data){
		throw new ServiceUnavailableException();
	}
*/
}
