package scc.cache;

import scc.data.RentalDAO;
import scc.data.house.HouseDAO;
import scc.db.CosmosDBLayer;
import scc.db.HouseDB;
import scc.db.RentalDB;

public class RentalService extends AbstractService<RentalDAO, RentalDB> {
    public RentalService() {
        super(RentalDAO.class, "rental:", CosmosDBLayer.getInstance().rentalDB);
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }
}
