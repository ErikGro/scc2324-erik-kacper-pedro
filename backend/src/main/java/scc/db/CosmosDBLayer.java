package scc.db;

import com.azure.cosmos.*;
import scc.data.Questions;
import scc.data.RentalDAO;
import scc.data.UserDAO;
import scc.data.house.HouseDAO;

public class CosmosDBLayer {
	private static CosmosDBLayer instance;
	private final CosmosClient client;
	public UserDB userDB;
	public HouseDB houseDB;
	public RentalDB rentalDB;
	public QuestionsDB questionsDB;
	
	public static synchronized CosmosDBLayer getInstance() {
		if(instance != null)
			return instance;

		instance = new CosmosDBLayer();

		return instance;
	}

	private CosmosDBLayer() {
		client = new CosmosClientBuilder()
				.endpoint(System.getenv("DB_CONNECTION_URL"))
				.key(System.getenv("DB_KEY"))
				//.directMode()
				.gatewayMode()
				// replace by .directMode() for better performance
				.consistencyLevel(ConsistencyLevel.SESSION)
				.connectionSharingAcrossClientsEnabled(true)
				.contentResponseOnWriteEnabled(true)
				.buildClient();

		CosmosDatabase db = client.getDatabase(System.getenv("DB_NAME"));

		questionsDB = new QuestionsDB(db.getContainer("questions"));
		userDB = new UserDB(db.getContainer("users"));
		houseDB = new HouseDB(db.getContainer("houses"));
		rentalDB = new RentalDB(db.getContainer("rental"));
	}

	public void close() {
		client.close();
	}
}
