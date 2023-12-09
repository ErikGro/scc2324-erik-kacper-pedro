package pt.unl.fct.di.scc;

import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import java.util.List;
import java.util.stream.Collectors;

import static dev.morphia.query.filters.Filters.eq;

public class MongoRentalCollection extends MongoAbstractCollection<RentalDAO> implements RentalContainer {
    MongoRentalCollection(Datastore datastore) {
        super(RentalDAO.class, datastore);
    }

    @Override
    synchronized public void deleteUserID(String id) {
        datastore.find(RentalDAO.class)
                .filter(eq("tenant_id", id))
                .delete(new DeleteOptions().multi(true));
    }

    @Override
    synchronized public ServiceResponse<List<RentalDAO>> getRentalsByUserID(String userID) {
        List<RentalDAO> rentals = datastore.find(RentalDAO.class)
                .filter(eq("tenant_id", userID))
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, rentals);
    }

    @Override
    synchronized public ServiceResponse<List<RentalDAO>> getRentalsByHouseID(String houseID) {
        List<RentalDAO> rentals = datastore.find(RentalDAO.class)
                .filter(eq("house_id", houseID))
                .stream()
                .collect(Collectors.toList());

        return new ServiceResponse<>(200, rentals);
    }
}
