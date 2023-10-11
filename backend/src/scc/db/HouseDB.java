package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.HouseDAO;

public class HouseDB extends DBContainer {
    HouseDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<HouseDAO> putHouse(HouseDAO house) {
        return container.createItem(house);
    }

    public CosmosPagedIterable<HouseDAO> getHouse(String id) {
        return container.queryItems("SELECT * FROM users WHERE users.id=\"" + id + "\"", new CosmosQueryRequestOptions(), HouseDAO.class);
    }
}
