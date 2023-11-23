package scc.persistence.db.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import scc.data.QuestionsDAO;
import scc.data.RentalDAO;
import scc.data.UserDAO;
import scc.data.house.HouseDAO;
import scc.persistence.db.*;

public class MongoDBLayer implements DBLayer {
    private static MongoDBLayer instance;

    private final MongoUserCollection userContainer;
    private final MongoHouseCollection houseContainer;
    private final MongoRentalCollection rentalContainer;
    private final MongoQuestionsCollection questionsContainer;


    public static synchronized MongoDBLayer getInstance() {
        if(instance != null)
            return instance;

        instance = new MongoDBLayer();

        return instance;
    }

    private MongoDBLayer() {
        // TODO: Replalce connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("scc");
            userContainer = new MongoUserCollection(database.getCollection("users", UserDAO.class));
            houseContainer = new MongoHouseCollection(database.getCollection("houses", HouseDAO.class));
            rentalContainer = new MongoRentalCollection(database.getCollection("rentals", RentalDAO.class));
            questionsContainer = new MongoQuestionsCollection(database.getCollection("questions", QuestionsDAO.class));
        }
    }

    @Override
    public UserContainer getUserContainer() {
        return userContainer;
    }

    @Override
    public HouseContainer getHouseContainer() {
        return houseContainer;
    }

    @Override
    public RentalContainer getRentalContainer() {
        return rentalContainer;
    }

    @Override
    public QuestionsContainer getQuestionsContainer() {
        return questionsContainer;
    }
}
