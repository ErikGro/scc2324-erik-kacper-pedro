package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.RentalDAO;
import scc.data.house.HouseDAO;

public class RentalDB extends AbstractDB<RentalDAO> {
    public RentalDB(CosmosContainer container) {
        super(container, RentalDAO.class);
    }

    public synchronized void deleteUserID(String id) {
        CosmosPagedIterable<RentalDAO> rentalsTenant = getRentalsByUserID(id);

        for (RentalDAO rental : rentalsTenant) {
            rental.setTenantID("DeletedUser");
            upsert(rental);
        }
    }
    public synchronized CosmosPagedIterable<RentalDAO> getRentalsByUserID(String id) {
        return container.queryItems("SELECT * FROM rental WHERE rental.tenantID=\"" + id + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);
    }
}
