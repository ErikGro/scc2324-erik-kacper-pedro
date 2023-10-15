package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemResponse;
import scc.data.RentalDAO;

public class RentalDB extends DBContainer {
    RentalDB(CosmosContainer container) {
        super(container);
    }

    public CosmosItemResponse<RentalDAO> putRental(RentalDAO rental) {
        return container.createItem(rental);
    }
}
