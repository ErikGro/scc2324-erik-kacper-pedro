package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.house.HouseDAO;

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
