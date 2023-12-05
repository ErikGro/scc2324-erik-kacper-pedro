package scc.persistence.db.mongo;

import dev.morphia.Datastore;
import scc.cache.ServiceResponse;
import scc.data.UserDAO;
import scc.persistence.db.UserContainer;

import static dev.morphia.query.filters.Filters.eq;


public class MongoUserCollection extends MongoAbstractCollection<UserDAO> implements UserContainer {
    MongoUserCollection(Datastore datastore) {
        super(UserDAO.class, datastore);
    }

    @Override
    public synchronized ServiceResponse<UserDAO> getByUsername(String username) {
        UserDAO user = datastore.find(UserDAO.class)
                .filter(eq("username", username))
                .first();

        if (user == null) {
            return new ServiceResponse<>(404);
        } else {
            return new ServiceResponse<>(200, user);
        }
    }
}
