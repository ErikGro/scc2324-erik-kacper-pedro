package scc.db;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;

public class CosmosDBLayer {
	private static final String CONNECTION_URL = "https://kmotyka68806.documents.azure.com:443/";
	private static final String DB_KEY = "nJKGRvbOHDNHSXr9PnwF8lfPQLk2Kjilbg7nF0enOYJ9ThvFBkQ40jnMeyaWP8PqhEng42QjDblfACDbzeHBnA==";
	private static final String DB_NAME = "sccbackend";
	
	private static CosmosDBLayer instance;

	public static synchronized CosmosDBLayer getInstance() {
		if(instance != null)
			return instance;

		CosmosClient client = new CosmosClientBuilder()
		         .endpoint(System.getenv("DB_CONNECTION_URL"))
		         .key(System.getenv("DB_KEY"))
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
	public QuestionDB questionDB;
	public AnswerDB answerDB;

	public AvailableMonthDB availableMonthDB;

	public CosmosDBLayer(CosmosClient client) {
		this.client = client;

		init();
	}
	
	private synchronized void init() {
		if( db != null)
			return;

		db = client.getDatabase(System.getenv("DB_NAME"));

		CosmosContainer housesContainer = db.getContainer("houses");
		houseDB = new HouseDB(housesContainer);

		CosmosContainer questionsContainer = db.getContainer("questions");
		questionDB = new QuestionDB(questionsContainer);

		CosmosContainer answersContainer = db.getContainer("answers");
		answerDB = new AnswerDB(answersContainer);
		userDB = new UserDB(db.getContainer("users"));
		houseDB = new HouseDB(db.getContainer("houses"));
		availableMonthDB = new AvailableMonthDB(db.getContainer("availableMonth"));
	}


	public void close() {
		client.close();
	}
}
