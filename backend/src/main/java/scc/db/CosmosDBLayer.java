package scc.db;

import com.azure.cosmos.*;

public class CosmosDBLayer {
	private static CosmosDBLayer instance;
	private final CosmosClient client;
	private final CosmosDatabase db;
	public UserDB userDB;
	public HouseDB houseDB;
	public QuestionDB questionDB;
	public AnswerDB answerDB;
	public RentalDB rentalDB;

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

		db = client.getDatabase(System.getenv("DB_NAME"));

		questionDB = new QuestionDB(db.getContainer("questions"));
		answerDB = new AnswerDB(db.getContainer("answers"));
		userDB = new UserDB(db.getContainer("users"));
		houseDB = new HouseDB(db.getContainer("houses"));
		rentalDB = new RentalDB(db.getContainer("rental"));
	}

	public void close() {
		client.close();
	}
}
