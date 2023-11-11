package scc.db;

import com.azure.cosmos.*;
import scc.utils.Constants;

public class CosmosDBLayer {
	private static CosmosDBLayer instance;
	private final CosmosClient client;
	private UserDB userDB;
	private HouseDB houseDB;
	private RentalDB rentalDB;
	private QuestionsDB questionsDB;
	
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
				//.directMode()
				.gatewayMode()
				// replace by .directMode() for better performance
				.consistencyLevel(ConsistencyLevel.SESSION)
				.connectionSharingAcrossClientsEnabled(true)
				.contentResponseOnWriteEnabled(true)
				.buildClient();

		CosmosDatabase db = client.getDatabase(Constants.getDBName());

		setQuestionsDB(new QuestionsDB(db.getContainer("questions")));
		setUserDB(new UserDB(db.getContainer("users")));
		setHouseDB(new HouseDB(db.getContainer("houses")));
		setRentalDB(new RentalDB(db.getContainer("rental")));
	}

	public void close() {
		client.close();
	}

	public UserDB getUserDB() {
		return userDB;
	}

	public void setUserDB(UserDB userDB) {
		this.userDB = userDB;
	}

	public HouseDB getHouseDB() {
		return houseDB;
	}

	public void setHouseDB(HouseDB houseDB) {
		this.houseDB = houseDB;
	}

	public RentalDB getRentalDB() {
		return rentalDB;
	}

	public void setRentalDB(RentalDB rentalDB) {
		this.rentalDB = rentalDB;
	}

	public QuestionsDB getQuestionsDB() {
		return questionsDB;
	}

	public void setQuestionsDB(QuestionsDB questionsDB) {
		this.questionsDB = questionsDB;
	}
}
