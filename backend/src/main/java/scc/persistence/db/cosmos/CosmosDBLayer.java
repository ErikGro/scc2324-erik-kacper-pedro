package scc.persistence.db.cosmos;

import com.azure.cosmos.*;
import scc.persistence.db.DBLayer;
import scc.utils.Constants;

public class CosmosDBLayer implements DBLayer {
	private static CosmosDBLayer instance;
	private final CosmosClient client;
	private final CosmosUserContainer userDB;
	private final CosmosHouseContainer cosmosHouseDB;
	private final CosmosRentalContainer cosmosRentalDB;
	private final CosmosQuestionsContainer cosmosQuestionsDB;
	
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

		cosmosQuestionsDB = new CosmosQuestionsContainer(db.getContainer("questions"));
		userDB = new CosmosUserContainer(db.getContainer("users"));
		cosmosHouseDB = new CosmosHouseContainer(db.getContainer("houses"));
		cosmosRentalDB = new CosmosRentalContainer(db.getContainer("rental"));
	}

	public CosmosUserContainer getUserContainer() {
		return userDB;
	}

	public CosmosHouseContainer getHouseContainer() {
		return cosmosHouseDB;
	}

	public CosmosRentalContainer getRentalContainer() {
		return cosmosRentalDB;
	}

	public CosmosQuestionsContainer getQuestionsContainer() {
		return cosmosQuestionsDB;
	}
}
