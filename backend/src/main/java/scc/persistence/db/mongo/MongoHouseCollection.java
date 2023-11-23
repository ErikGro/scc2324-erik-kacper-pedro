package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import scc.cache.ServiceResponse;
import scc.data.house.HouseDAO;
import scc.persistence.db.HouseContainer;

import java.util.List;

public class MongoHouseCollection extends MongoAbstractCollection<HouseDAO> implements HouseContainer {
    MongoHouseCollection(MongoCollection<HouseDAO> collection) {
        super(collection);
    }

    @Override
    synchronized public void deleteUserID(String id) {

    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getHousesByUserID(String id) {
        return null;
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getHousesByCity(String name) {
        return null;
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getHousesByCityAndPeriod(String name, String startDate, String endDate) {
        return null;
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getDiscountedHousesNearFuture() {
        return null;
    }
}
