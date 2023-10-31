package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.house.HouseDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HouseDB extends AbstractDB<HouseDAO> {

    public HouseDB(CosmosContainer container) {
        super(container, HouseDAO.class);
    }

    public CosmosPagedIterable<HouseDAO> getHousesByUserID(String id) {
        String query = "SELECT * FROM houses WHERE houses.ownerID=\"" + id + "\"";
        return container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);
    }
    
    public void deleteUserID(String id) {
        CosmosPagedIterable<HouseDAO> houses = getHousesByUserID(id);
        for (HouseDAO house : houses) {
            house.setOwnerID("DeletedUser");
            upsert(house);
        }
    }
    
    public CosmosPagedIterable<HouseDAO> getHousesByCity(String name) {
        String query = "SELECT * FROM houses WHERE houses.address.city=\"" + name + "\"";
        return container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public CosmosPagedIterable<HouseDAO> getHousesByCityAndPeriod(String name, String startDate, String endDate) {
        String query = "SELECT * FROM houses WHERE houses.address.city=\"" + name + "\" AND EXISTS (SELECT VALUE p FROM p IN houses.availablePeriods WHERE p.startDate >= \"" + startDate + "\" AND p.startDate <= \"" + endDate + "\")";
        return container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public CosmosPagedIterable<HouseDAO> getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        String query = "SELECT * FROM houses WHERE EXISTS (SELECT VALUE p FROM p IN houses.availablePeriods WHERE p.startDate >= \"" + startDate + "\" AND p.startDate <= \"" + endDate + "\" AND IS_DEFINED(p.promotionPrice))";

        return container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public boolean hasHouse(String id) {
        CosmosPagedIterable<HouseDAO> res = getHousesByUserID(id);
        return res.iterator().hasNext();
    }

    public boolean houseExists(String id) {
        CosmosPagedIterable<HouseDAO> res = container.queryItems("SELECT * FROM houses WHERE houses.id=\"" + id + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
        return res.iterator().hasNext();
    }
}
