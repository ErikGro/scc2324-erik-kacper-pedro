package scc.persistence.db.mongo;

import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.Query;
import scc.cache.ServiceResponse;
import scc.data.house.HouseDAO;
import scc.persistence.db.HouseContainer;

import java.util.List;
import java.util.stream.Collectors;

import static dev.morphia.query.filters.Filters.eq;

public class MongoHouseCollection extends MongoAbstractCollection<HouseDAO> implements HouseContainer {
    MongoHouseCollection(Datastore datastore) {
        super(HouseDAO.class, datastore);
    }

    @Override
    synchronized public void deleteUserID(String id) {
        datastore.find(HouseDAO.class)
                .filter(eq("owner_id", id))
                .delete(new DeleteOptions().multi(true));
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getHousesByUserID(String id) {
        List<HouseDAO> houses = datastore.find(HouseDAO.class)
                .filter(eq("owner_id", id))
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, houses);
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getHousesByCity(String name) {
        List<HouseDAO> houses = datastore.find(HouseDAO.class)
                .filter(eq("address.city", name))
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, houses);
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getHousesByCityAndPeriod(String name, String startDate, String endDate) {
        Query<HouseDAO> query = datastore.find(HouseDAO.class);
        query.filter(eq("address.city", name));
        // TODO:
        return new ServiceResponse<>(200, query.stream().collect(Collectors.toList()));
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getDiscountedHousesNearFuture() {
        // TODO:
        return null;
    }
}
