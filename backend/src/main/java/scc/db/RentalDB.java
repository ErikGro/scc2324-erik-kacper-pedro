package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.RentalDAO;

public class RentalDB extends AbstractDB<RentalDAO> {
    public RentalDB(CosmosContainer container) {
        super(container, RentalDAO.class);
    }

    public CosmosPagedIterable<RentalDAO> getRentalsByUserID(String id) {
        return container.queryItems("SELECT * FROM rental WHERE rental.tenantID=\"" + id + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);
    }
}
