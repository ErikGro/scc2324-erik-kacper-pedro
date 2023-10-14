package scc.srv;

import scc.db.*;

import java.util.List;
import java.util.Locale;
import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import redis.clients.jedis.Jedis;
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
		UserDB db=db0.userDB;
		String id = Double.toString(Math.random());
		CosmosItemResponse<UserDAO> res = null;
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setPhotoId(data.getPhotoId());
		u.setHouseIds(data.getHouseIds());

		res = db.putUser(u);
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
		UserDB db=db0.userDB;
		UserDAO u=  db.getUserById(id).iterator().next();
		u.getHouseIds();
		
		String[] combinedHouseIds = concatenate(u.getHouseIds(), house.getHouseIds());
		//
		// Now you can set the combined IDs back to the user or save them or whatever you intend
		u.setHouseIds(combinedHouseIds);

		// Assuming you have a method to update the user in the DB
		db.updateUser(u);

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
		return  db.getUserById(id)!=null;
	}
	
	@Path("/secret")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllUsers(){
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		//TODO: add compatibility with houses to show that the user has been deleted
		db.deleteAllUsers();
		return Response.ok("D:").build();
		//throw new ServiceUnavailableException();

	}


	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@QueryParam("id") String id){
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		//TODO: add compatibility with houses to show that the user has been deleted
		db.delUserById(id);
		return Response.ok(id).build();
		//throw new ServiceUnavailableException();

	}
	//TODO: transactions
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@QueryParam("id") String id, User data){
		if(!userExists(id))
			return Response.status(400).entity("No such user").build();
		
		CosmosDBLayer db0=CosmosDBLayer.getInstance();
		UserDB db=db0.userDB;
		
		db.delUserById(id);
		UserDAO u = new UserDAO();
		u.setId(id);
		u.setName(data.getName());
		u.setPwd(data.getPwd());
		u.setPhotoId(data.getPhotoId());
		u.setHouseIds(data.getHouseIds());

		 db.putUser(u);
		return Response.ok(id).build();
	}

}
