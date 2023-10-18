package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.RentalDAO;

public class RentalDB extends DBContainer {
    RentalDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<RentalDAO> upsertRental(RentalDAO rental) {
        return container.upsertItem(rental);
    }

    public CosmosItemResponse<RentalDAO> getRentalByID(String id) {
        return container.readItem(id, new PartitionKey(id), RentalDAO.class);
    }

    public CosmosPagedIterable<RentalDAO> getRentalsByUserID(String id) {
        return container.queryItems("SELECT * FROM rental WHERE rental.tenantID=\"" + id + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);
    }

    public CosmosItemResponse<Object> deleteRental(String id) {
        return container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
    }
}
