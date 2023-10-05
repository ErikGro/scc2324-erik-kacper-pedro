package scc.utils;

import java.util.Locale;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;

import scc.data.UserDAO;
import scc.db.CosmosDBLayer;

/**
 * Standalone program for accessing the database
 *
 */
public class TestUsers
{
	public static void main(String[] args) {
		System.setProperty(org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "Error");

		try {
			Locale.setDefault(Locale.US);
			CosmosDBLayer db = CosmosDBLayer.getInstance();
			String id = "0:" + System.currentTimeMillis();
			CosmosItemResponse<UserDAO> res = null;
			UserDAO u = new UserDAO();
			u.setId(id);
			u.setName("SCC " + id);
			u.setPwd("super_secret");
			u.setPhotoId("0:34253455");
			u.setHouseIds(new String[0]);

			res = db.putUser(u);
			System.out.println( "Put result");
			System.out.println( res.getStatusCode());
			System.out.println( res.getItem());

			System.out.println( "Get for id = " + id);
			CosmosPagedIterable<UserDAO> resGet = db.getUserById(id);
			for( UserDAO e: resGet) {
				System.out.println( e);
			}

			System.out.println( "Get for all ids");
			resGet = db.getUsers();
			for( UserDAO e: resGet) {
				System.out.println( e);
			}

			// Now, let's create and delete
			id = "0:" + System.currentTimeMillis();
			res = null;
			u = new UserDAO();
			u.setId(id);
			u.setName("SCC " + id);
			u.setPwd("super_secret");
			u.setPhotoId("0:34253455");
			u.setHouseIds(new String[0]);

			res = db.putUser(u);
			System.out.println( "Put result");
			System.out.println( res.getStatusCode());
			System.out.println( res.getItem());
			System.out.println( "Get for id = " + id);

			System.out.println( "Get by id result");
			resGet = db.getUserById(id);
			for( UserDAO e: resGet) {
				System.out.println( e);
			}
			
			System.out.println( "Delte user");
			db.delUserById(id);

			System.out.println( "Get by id result");
			resGet = db.getUserById(id);
			for( UserDAO e: resGet) {
				System.out.println( e);
			}

			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


