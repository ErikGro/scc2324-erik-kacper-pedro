package scc.db;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import scc.utils.Env;

public class CosmosDBLayer {
	private static CosmosDBLayer instance;

	public static synchronized CosmosDBLayer getInstance() {
		if(instance != null)
			return instance;

		CosmosClient client = new CosmosClientBuilder()
		         .endpoint(Env.getInstance().getDBConnectionUrl())
		         .key(Env.getInstance().getDBKey())
		         //.directMode()
		         .gatewayMode()		
		         // replace by .directMode() for better performance
		         .consistencyLevel(ConsistencyLevel.SESSION)
		         .connectionSharingAcrossClientsEnabled(true)
		         .contentResponseOnWriteEnabled(true)
		         .buildClient();
		instance = new CosmosDBLayer(client);

		return instance;
	}
	
	private CosmosClient client;
	private CosmosDatabase db;

	public UserDB userDB;
	public HouseDB houseDB;

	public CosmosDBLayer(CosmosClient client) {
		this.client = client;

		init();
	}
	
	private synchronized void init() {
		if( db != null)
			return;
		db = client.getDatabase(Env.getInstance().getDBName());

		CosmosContainer usersContainer = db.getContainer("users");
		userDB = new UserDB(usersContainer);

		CosmosContainer housesContainer = db.getContainer("houses");
		houseDB = new HouseDB(housesContainer);
	}


	public void close() {
		client.close();
	}
}
