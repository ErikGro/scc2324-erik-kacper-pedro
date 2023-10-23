package scc.srv;

import scc.db.*;
import scc.db.blob.BlobLayer;

import java.util.List;
import java.util.Locale;
import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import scc.cache.RedisCache;
import scc.data.HouseIds;
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
		ObjectMapper mapper = new ObjectMapper();
		Locale.setDefault(Locale.US);
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB<UserDAO> db=db0.userDB;
		String id = Double.toString(Math.random());
		CosmosItemResponse<UserDAO> res = null;
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setHouseIds(data.getHouseIds());

		res = db.upsert(u);
		try (Jedis jedis = RedisCache.getCachePool().getResource()) {
		    jedis.set("user:"+id, mapper.writeValueAsString(u));
		    String res1 = jedis.get("user:"+id);
		    	// How to convert string to object
		    UserDAO uread = mapper.readValue(res1, UserDAO.class);
	    	System.out.println("GET value = " + res1);
	    					
		    Long cnt = jedis.lpush("MostRecentUsers", mapper.writeValueAsString(u));
		    if (cnt > 5)
		        jedis.ltrim("MostRecentUsers", 0, 4);
		    
		    List<String> lst = jedis.lrange("MostRecentUsers", 0, -1);
	    	System.out.println("MostRecentUsers");
		    for( String s : lst)
		    	System.out.println(s);
		   
		    cnt = jedis.incr("NumUsers");
		    System.out.println( "Num users : " + cnt);
		}
	 catch (Exception e) {
		e.printStackTrace();
	}
		data.setId(id);
		return Response.ok(data.getId()).build();
	}
	@Path("/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addHouseid(@QueryParam("id") String id, HouseIds house ) {
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB<UserDAO> db=db0.userDB;
		UserDAO u=  db.getByID(id).getItem();
		u.getHouseIds();
		
		String[] combinedHouseIds = concatenate(u.getHouseIds(), house.getHouseIds());
		//
		// Now you can set the combined IDs back to the user or save them or whatever you intend
		u.setHouseIds(combinedHouseIds);

		// Assuming you have a method to update the user in the DB
		db.upsert(u);

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
		Locale.setDefault(Locale.UK);
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		return  db.getByID(id)!=null;
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
	    
	    CosmosDBLayer db0 = CosmosDBLayer.getInstance();
	    UserDB<UserDAO> db = db0.userDB;
	    
	    try (Jedis jedis = RedisCache.getCachePool().getResource()) {
	        // Delete user from Redis
	        jedis.del("user:" + id);
	        
	        // Update other Redis values if necessary. For example, if you want to decrement NumUsers:
	        jedis.decr("NumUsers");
	        
	        // You can also remove the user from the "MostRecentUsers" list if they exist there:
	        String userString = jedis.get("user:" + id);
	        if (userString != null)
	        	jedis.lrem("MostRecentUsers", 1, userString);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    // Now delete from CosmosDB
	    db.deleteByID(id);
	    return Response.ok("User " + id + " deleted.").build();
	}

	//TODO: transactions
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@QueryParam("id") String id, User data){
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB<UserDAO> db=db0.userDB;
		
		db.deleteByID(id);
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setHouseIds(data.getHouseIds());

		 db.upsert(u);
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
}
