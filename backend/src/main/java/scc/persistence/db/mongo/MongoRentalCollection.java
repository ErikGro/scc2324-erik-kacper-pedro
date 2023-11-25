package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import scc.cache.ServiceResponse;
import scc.data.RentalDAO;
import scc.persistence.db.RentalContainer;

import java.util.List;

public class MongoRentalCollection extends MongoAbstractCollection<RentalDAO> implements RentalContainer {
    MongoRentalCollection(MongoCollection<RentalDAO> collection) {
        super(collection);
    }

    @Override
    synchronized public void deleteUserID(String id) {

    }

    @Override
    synchronized public ServiceResponse<List<RentalDAO>> getRentalsByUserID(String userID) {
        return null;
    }

    @Override
    synchronized public ServiceResponse<List<RentalDAO>> getRentalsByHouseID(String houseID) {
        return null;
    }
}
