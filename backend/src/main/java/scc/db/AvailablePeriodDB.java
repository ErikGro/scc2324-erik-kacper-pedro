package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.house.AvailablePeriodDAO;

public class AvailablePeriodDB extends DBContainer {
    AvailablePeriodDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<AvailablePeriodDAO> createAvailablePeriod(AvailablePeriodDAO period) {
        return container.createItem(period);
    }

    public void deletePeriodsForHouse(String houseID) {
        CosmosPagedIterable<AvailablePeriodDAO> periods = getAvailablePeriodsForHouse(houseID);
        periods.forEach(availablePeriodDAO -> {
            String id = availablePeriodDAO.getId();
            container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
        });
    }

    public CosmosPagedIterable<AvailablePeriodDAO> getAvailablePeriodsForHouse(String id) {
        return container.queryItems("SELECT * FROM availablePeriod WHERE availablePeriod.availableMonth.houseID=\"" + id + "\"", new CosmosQueryRequestOptions(), AvailablePeriodDAO.class);
    }
}

