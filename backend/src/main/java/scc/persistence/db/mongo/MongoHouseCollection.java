package scc.persistence.db.mongo;

import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import scc.cache.ServiceResponse;
import scc.data.house.HouseDAO;
import scc.persistence.db.HouseContainer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static dev.morphia.query.filters.Filters.*;

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
        List<HouseDAO> houses = datastore.find(HouseDAO.class)
                .filter(eq("address.city", name),
                        elemMatch("available_periods", gte("start_date", startDate), lte("start_date", endDate)))
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, houses);
    }

    @Override
    synchronized public ServiceResponse<List<HouseDAO>> getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        List<HouseDAO> houses = datastore.find(HouseDAO.class)
                .filter(elemMatch("available_periods", gte("start_date", startDate), lte("start_date", endDate)))
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, houses);
    }
}
