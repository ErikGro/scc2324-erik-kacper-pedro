package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import scc.cache.ServiceResponse;
import scc.data.UserDAO;
import scc.persistence.db.UserContainer;

import static com.mongodb.client.model.Filters.eq;

public class MongoUserCollection extends MongoAbstractCollection<UserDAO> implements UserContainer {
    MongoUserCollection(MongoCollection<UserDAO> collection) {
        super(collection);
    }

    @Override
    public synchronized ServiceResponse<UserDAO> getByUsername(String username) {
        return getResponse(collection.find(eq("username", username)).first());
    }
}
