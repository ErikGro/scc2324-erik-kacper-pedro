package scc.persistence.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.cache.ServiceResponse;
import java.util.List;

import scc.data.house.HouseDAO;
import scc.persistence.db.HouseContainer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Collectors;

public class CosmosHouseContainer extends CosmosAbstractContainer<HouseDAO> implements HouseContainer {

    public CosmosHouseContainer(CosmosContainer container) {
        super(container, HouseDAO.class);
    }

    public synchronized ServiceResponse<List<HouseDAO>> getHousesByUserID(String id) {
        String query = "SELECT * FROM houses WHERE houses.ownerID=\"" + id + "\"";
        CosmosPagedIterable<HouseDAO> response = container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }
    
    public synchronized void deleteUserID(String id) {
        ServiceResponse<List<HouseDAO>> houses = getHousesByUserID(id);
        if (houses.getItem().isPresent()) {
            for (HouseDAO house : houses.getItem().get()) {
                house.setOwnerID("DeletedUser");
                upsert(house);
            }
        }
    }
    
    public synchronized ServiceResponse<List<HouseDAO>> getHousesByCity(String name) {
        String query = "SELECT * FROM houses WHERE houses.address.city=\"" + name + "\"";
        CosmosPagedIterable<HouseDAO> response =  container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }

    public synchronized ServiceResponse<List<HouseDAO>> getHousesByCityAndPeriod(String name, String startDate, String endDate) {
        String query = "SELECT * FROM houses WHERE houses.address.city=\"" + name + "\" AND EXISTS (SELECT VALUE p FROM p IN houses.availablePeriods WHERE p.startDate >= \"" + startDate + "\" AND p.endDate <= \"" + endDate + "\")";
        CosmosPagedIterable<HouseDAO> response =  container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }

    public synchronized ServiceResponse<List<HouseDAO>> getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        String query = "SELECT * FROM houses WHERE EXISTS (SELECT VALUE p FROM p IN houses.availablePeriods WHERE p.startDate >= \"" + startDate + "\" AND p.startDate <= \"" + endDate + "\" AND IS_DEFINED(p.promotionPrice))";

        CosmosPagedIterable<HouseDAO> response =  container.queryItems(query, new CosmosQueryRequestOptions(), HouseDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }

    public synchronized boolean hasHouse(String userID) {
        ServiceResponse<List<HouseDAO>> res = getHousesByUserID(userID);
        return res.getItem().isPresent() && !res.getItem().get().isEmpty();
    }

    public synchronized  boolean houseExists(String id) {
        CosmosPagedIterable<HouseDAO> res = container.queryItems("SELECT * FROM houses WHERE houses.id=\"" + id + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
        return res.iterator().hasNext();
    }
}
