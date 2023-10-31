package scc.cache;

import scc.data.house.HouseDAO;
import scc.db.CosmosDBLayer;
import scc.db.HouseDB;

public class HouseService extends AbstractService<HouseDAO, HouseDB> {
    public HouseService() {
        super(HouseDAO.class, "house:", CosmosDBLayer.getInstance().houseDB);
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }
}
