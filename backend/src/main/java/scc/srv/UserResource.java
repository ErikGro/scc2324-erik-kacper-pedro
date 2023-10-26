package scc.srv;

import scc.db.*;
import scc.db.blob.BlobLayer;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import scc.cache.UserService;
import scc.cache.RedisCache;
import scc.cache.ServiceResponse;
import scc.data.HouseIds;
import scc.data.User;
import scc.data.UserDAO;

/**
 * Class with control endpoints.
 */
@Path("/user")
public class UserResource
{

	private final UserService UserService = new UserService();

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
		
		
		String id = UUID.randomUUID().toString();
		ServiceResponse<UserDAO> res = null;
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setHouseIds(data.getHouseIds());

		res = UserService.upsert(u);
		
		data.setId(id);
		return Response.ok(data.getId()).build();
	}
	@Path("/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addHouseid(@QueryParam("id") String id, HouseIds house ) {
		
		
		UserDAO u=  UserService.getByID(id).getItem().get();
		u.getHouseIds();
		
		String[] combinedHouseIds = concatenate(u.getHouseIds(), house.getHouseIds());
		//
		// Now you can set the combined IDs back to the user or save them or whatever you intend
		u.setHouseIds(combinedHouseIds);

		// Assuming you have a method to update the user in the DB
		UserService.upsert(u);

		return Response.ok(u.getHouseIds()).build(); // Return appropriate response
	}

	private String[] concatenate(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] result = new String[aLen + bLen];
		System.arraycopy(a, 0, result, 0, aLen);
		System.arraycopy(b, 0, result, aLen, bLen);
		return result;
	}
	//Public method to use when checking that the id belongs to a user
	public Boolean userExists(String id) {
		
		return  UserService.getByID(id).getItem().isEmpty();
	}
	
@Path("/secret")
@DELETE
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response deleteAllUsers() {
    CosmosDBLayer db0 = CosmosDBLayer.getInstance();
    UserDB db = db0.userDB;

    try (Jedis jedis = RedisCache.getCachePool().getResource()) {
        String cursor = "0";
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, new ScanParams().match("user:*").count(100));
            List<String> keys = scanResult.getResult();
            
            if (!keys.isEmpty()) {
                jedis.del(keys.toArray(new String[keys.size()]));
            }
            
            cursor = scanResult.getCursor();
        } while (!cursor.equals("0"));
        
        // Reset counters and lists related to users
        jedis.del("NumUsers");
        jedis.del("MostRecentUsers");
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    // Now delete from CosmosDB
    db.deleteAllUsers();
    return Response.ok("All users deleted.").build();
}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@QueryParam("id") String id) {
	    if (!userExists(id))
	        return Response.status(400).entity("No such user").build();
	    UserService.deleteByID(id);
	    return Response.ok("User " + id + " deleted.").build();
	}

	//TODO: transactions
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@QueryParam("id") String id, User data){
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
	
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setHouseIds(data.getHouseIds());
		
		 UserService.upsert(u);
		return Response.ok(id).build();
	}

	@Path("/{id}/photo")
	@POST
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadPhoto(@PathParam("id") String id, byte[] photo) {
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
		
		BlobLayer blobLayer = BlobLayer.getInstance();
		blobLayer.usersContainer.uploadImage(id, photo);
		
		return Response.ok(id).build();
	}

	@Path("/{id}/photo")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getPhoto(@PathParam("id") String id) {
		if(!userExists(id))
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
	
	public UserDAO getUser(String id) {
		if(!userExists(id))
			return null;
		return UserService.getByID(id).getItem().get();
	}
}
