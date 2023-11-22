package scc.persistence.db;

import com.azure.cosmos.*;
import scc.utils.Constants;

public class CosmosDBLayer {
	private static CosmosDBLayer instance;
	private final CosmosClient client;
	private final UserDB userDB;
	private final HouseDB houseDB;
	private final RentalDB rentalDB;
	private final QuestionsDB questionsDB;
	
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

		questionsDB = new QuestionsDB(db.getContainer("questions"));
		userDB = new UserDB(db.getContainer("users"));
		houseDB = new HouseDB(db.getContainer("houses"));
		rentalDB = new RentalDB(db.getContainer("rental"));
	}

	public UserDB getUserDB() {
		return userDB;
	}

	public HouseDB getHouseDB() {
		return houseDB;
	}

	public RentalDB getRentalDB() {
		return rentalDB;
	}

	public QuestionsDB getQuestionsDB() {
		return questionsDB;
	}
}
