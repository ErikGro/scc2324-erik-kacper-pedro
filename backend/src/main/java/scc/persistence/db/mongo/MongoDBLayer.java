package scc.persistence.db.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import scc.data.QuestionsDAO;
import scc.data.RentalDAO;
import scc.data.UserDAO;
import scc.data.house.HouseDAO;
import scc.persistence.db.*;
import scc.utils.Constants;

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
        try (MongoClient mongoClient = MongoClients.create(Constants.getMongoDBConnectionString())) {
            MongoDatabase db = mongoClient.getDatabase("scc");

            userContainer = new MongoUserCollection(db.getCollection("users", UserDAO.class));
            houseContainer = new MongoHouseCollection(db.getCollection("houses", HouseDAO.class));
            rentalContainer = new MongoRentalCollection(db.getCollection("rentals", RentalDAO.class));
            questionsContainer = new MongoQuestionsCollection(db.getCollection("questions", QuestionsDAO.class));
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
