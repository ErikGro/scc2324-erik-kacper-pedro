package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import scc.cache.ServiceResponse;
import scc.data.UserDAO;
import scc.persistence.db.UserContainer;

public class MongoUserCollection extends MongoAbstractCollection<UserDAO> implements UserContainer {
    MongoUserCollection(MongoCollection<UserDAO> collection) {
        super(collection);
    }

    @Override
    public ServiceResponse<UserDAO> getByUsername(String username) {
        return null;
    }
}
