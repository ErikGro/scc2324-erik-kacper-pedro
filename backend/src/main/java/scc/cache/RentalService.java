package scc.cache;

import scc.data.RentalDAO;
import scc.persistence.db.cosmos.CosmosDBLayer;
import scc.persistence.db.cosmos.CosmosRentalDB;
import java.util.List;

public class RentalService extends AbstractService<RentalDAO, CosmosRentalDB> {
    public RentalService() {
        super(RentalDAO.class, "rental:", CosmosDBLayer.getInstance().getRentalDB());
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }

    public ServiceResponse<List<RentalDAO>> getRentalsForHouse(String houseID) {
        return db.getRentalsByHouseID(houseID);
    }
}
