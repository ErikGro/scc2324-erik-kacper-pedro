package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.house.HouseDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HouseDB extends DBContainer {
    HouseDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<HouseDAO> putHouse(HouseDAO house) {
        return container.upsertItem(house);
    }

    public CosmosPagedIterable<HouseDAO> getHousesByUserID(String id) {
        return container.queryItems("SELECT * FROM houses WHERE houses.ownerID=\"" + id + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public CosmosPagedIterable<HouseDAO> getHousesByCity(String name) {
        return container.queryItems("SELECT * FROM houses WHERE houses.address.city=\"" + name + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public CosmosPagedIterable<HouseDAO> getHousesByCityAndPeriod(String name, String startDate, String endDate) {
        return container.queryItems("SELECT * FROM houses INNER JOIN availablePeriod ON houses.id=availablePeriod.houseID WHERE houses.address.city=\"" + name + "\" AND houses.startDate>=\"" + startDate + "\" AND houses.endDate>=\"" + endDate + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public CosmosPagedIterable<HouseDAO> getDiscountedHousesNearFuture() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        String startDate = dateFormat.format(cal.getTime());

        cal.add(Calendar.MONTH, 3);
        String endDate = dateFormat.format(cal.getTime());

        return container.queryItems("SELECT * FROM houses INNER JOIN availablePeriod ON houses.id=availablePeriod.houseID WHERE houses.startDate>=\"" + startDate + "\" AND houses.endDate>=\"" + endDate + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
    }

    public CosmosItemResponse<HouseDAO> getHouseByID(String id) {
        return container.readItem(id, new PartitionKey(id), HouseDAO.class);
    }


    public CosmosItemResponse<Object> deleteHouse(String id) {
        return container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
    }

    public boolean hasHouse(String id) {
        CosmosPagedIterable<HouseDAO> res = getHousesByUserID(id);
        return res.iterator().hasNext();
    }
}
