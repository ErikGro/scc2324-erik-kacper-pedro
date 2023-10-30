package scc.srv;

import scc.db.blob.BlobLayer;

import java.util.ArrayList;

import java.util.Optional;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import scc.cache.ServiceResponse;
import scc.cache.UserService;
import scc.data.HouseIds;

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
	private final UserService userService = new UserService();
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(UserDAO data) {

		String id = UUID.randomUUID().toString();
		UserDAO u = new UserDAO(id, data.getName(), data.getPwd(), data.getHouseIds());

		ServiceResponse<UserDAO> res = userService.upsert(u);

		return Response.status(res.getStatusCode()).build();
	}

	@Path("/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addHouseid(@QueryParam("id") String id, HouseIds house ) {
		
		Optional<UserDAO> user =  userService.getByID(id).getItem();
		if(user.isEmpty())
			return Response.status(400).entity("No such user").build();

		ArrayList<String> combinedHouseIds = user.get().getHouseIds();
		combinedHouseIds.addAll(house.getHouseIds());

		user.get().setHouseIds(combinedHouseIds);
		userService.upsert(user.get());

		return Response.ok(user.get().getHouseIds()).build(); 
	}
	

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@QueryParam("id") String id) {

		Optional<UserDAO> user =  userService.getByID(id).getItem();
		if(user.isEmpty())
			return Response.status(400).entity("No such user").build();

		//TODO: add compatibility with houses to show that the user has been deleted
		userService.deleteByID(id);
		return Response.ok(id).build();
		//throw new ServiceUnavailableException();

	}

	//TODO: transactions
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@QueryParam("id") String id, UserDAO data){

		data.setId(id);
		ServiceResponse<UserDAO> res = userService.upsert(data);

		return Response.status(res.getStatusCode()).build();
	}

	@Path("/{id}/photo")
	@POST
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadPhoto(@PathParam("id") String id, byte[] photo) {

		Optional<UserDAO> user =  userService.getByID(id).getItem();
		if(user.isEmpty())
			return Response.status(400).entity("No such user").build();
		
		BlobLayer blobLayer = BlobLayer.getInstance();
		blobLayer.usersContainer.uploadImage(id, photo);
		
		return Response.ok(id).build();
	}

	@Path("/{id}/photo")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getPhoto(@PathParam("id") String id) {
		
		Optional<UserDAO> user =  userService.getByID(id).getItem();
		if(user.isEmpty())
			return Response.status(400).entity("No such user").build();
		
		BlobLayer blobLayer = BlobLayer.getInstance();

		byte[] photo;
		try {
			photo = blobLayer.usersContainer.getImage(id);
		} catch (Exception e) {
			return Response.status(404).entity("No photo found").build();
		}
		
		return Response.ok(photo).build();
	}
}
