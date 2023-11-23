package scc.cache;

import scc.data.RentalDAO;
import scc.persistence.db.RentalContainer;
import scc.persistence.db.mongo.MongoDBLayer;

import java.util.List;

public class RentalService extends AbstractService<RentalDAO, RentalContainer> {
    public RentalService() {
        super(RentalDAO.class, "rental:", MongoDBLayer.getInstance().getRentalContainer());
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }

    public ServiceResponse<List<RentalDAO>> getRentalsForHouse(String houseID) {
        return db.getRentalsByHouseID(houseID);
    }
}
