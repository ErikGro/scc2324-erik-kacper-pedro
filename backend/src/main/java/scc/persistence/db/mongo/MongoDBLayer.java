package scc.persistence.db.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import scc.persistence.db.*;
import scc.utils.Constants;

public class MongoDBLayer implements DBLayer {
    private static MongoDBLayer instance;
    final Datastore datastore;

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
        this.datastore = Morphia.createDatastore(MongoClients.create(Constants.getMongoDBConnectionString()));
        userContainer = new MongoUserCollection(this.datastore);
        houseContainer = new MongoHouseCollection(this.datastore);
        rentalContainer = new MongoRentalCollection(this.datastore);
        questionsContainer = new MongoQuestionsCollection(this.datastore);
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
