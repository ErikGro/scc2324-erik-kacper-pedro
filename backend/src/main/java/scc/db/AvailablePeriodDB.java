package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.house.AvailablePeriodDAO;

public class AvailablePeriodDB extends DBContainer {
    AvailablePeriodDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<AvailablePeriodDAO> putAvailablePeriod(AvailablePeriodDAO period) {
        return container.createItem(period);
    }

    public CosmosPagedIterable<AvailablePeriodDAO> getAvailablePeriodsForHouse(String id) {
        return container.queryItems("SELECT * FROM availablePeriod WHERE availableMonth.houseID=\"" + id + "\"", new CosmosQueryRequestOptions(), AvailablePeriodDAO.class);
    }
}

