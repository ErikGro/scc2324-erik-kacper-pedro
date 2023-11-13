package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.RentalDAO;

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
    public synchronized CosmosPagedIterable<RentalDAO> getRentalsByUserID(String userID) {
        return container.queryItems("SELECT * FROM rental WHERE rental.tenantID=\"" + userID + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);
    }

    public synchronized CosmosPagedIterable<RentalDAO> getRentalsByHouseID(String houseID) {
        return container.queryItems("SELECT * FROM rental WHERE rental.houseID=\"" + houseID + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);
    }
}
