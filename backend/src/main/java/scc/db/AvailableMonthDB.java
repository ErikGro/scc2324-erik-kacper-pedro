package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.house.AvailableMonthDAO;

public class AvailableMonthDB extends DBContainer {
    AvailableMonthDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<AvailableMonthDAO> putAvailableMonth(AvailableMonthDAO month) {
        return container.createItem(month);
    }

    public CosmosPagedIterable<AvailableMonthDAO> getAvailableMonthForHouse(String id) {
        return container.queryItems("SELECT * FROM availableMonth WHERE availableMonth.houseID=\"" + id + "\"", new CosmosQueryRequestOptions(), AvailableMonthDAO.class);
    }
}

