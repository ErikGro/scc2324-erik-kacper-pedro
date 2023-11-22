package scc.persistence.db.cosmos;

import com.azure.cosmos.*;
import scc.persistence.db.DBLayer;
import scc.utils.Constants;

public class CosmosDBLayer implements DBLayer {
	private static CosmosDBLayer instance;
	private final CosmosClient client;
	private final CosmosUserDB userDB;
	private final CosmosHouseDB cosmosHouseDB;
	private final CosmosRentalDB cosmosRentalDB;
	private final CosmosQuestionsDB cosmosQuestionsDB;
	
	public static synchronized CosmosDBLayer getInstance() {
		if(instance != null)
			return instance;

		instance = new CosmosDBLayer();

		return instance;
	}

	private CosmosDBLayer() {
		client = new CosmosClientBuilder()
				.endpoint(Constants.getDBConnectionURL())
				.key(Constants.getDBKey())
				.gatewayMode() //.directMode()
				.consistencyLevel(ConsistencyLevel.SESSION)
				.connectionSharingAcrossClientsEnabled(true)
				.contentResponseOnWriteEnabled(true)
				.buildClient();

		CosmosDatabase db = client.getDatabase(Constants.getDBName());

		cosmosQuestionsDB = new CosmosQuestionsDB(db.getContainer("questions"));
		userDB = new CosmosUserDB(db.getContainer("users"));
		cosmosHouseDB = new CosmosHouseDB(db.getContainer("houses"));
		cosmosRentalDB = new CosmosRentalDB(db.getContainer("rental"));
	}

	public CosmosUserDB getUserDB() {
		return userDB;
	}

	public CosmosHouseDB getHouseDB() {
		return cosmosHouseDB;
	}

	public CosmosRentalDB getRentalDB() {
		return cosmosRentalDB;
	}

	public CosmosQuestionsDB getQuestionsDB() {
		return cosmosQuestionsDB;
	}
}
