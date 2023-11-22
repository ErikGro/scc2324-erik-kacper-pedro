package scc.persistence.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.cache.ServiceResponse;
import scc.data.RentalDAO;
import scc.persistence.db.RentalDB;

import java.util.List;
import java.util.stream.Collectors;

public class CosmosRentalDB extends CosmosAbstractDB<RentalDAO> implements RentalDB<RentalDAO> {
    public CosmosRentalDB(CosmosContainer container) {
        super(container, RentalDAO.class);
    }

    public synchronized void deleteUserID(String id) {
        ServiceResponse<List<RentalDAO>> rentalsTenant = getRentalsByUserID(id);

        if (rentalsTenant.getItem().isPresent()) {
            for (RentalDAO rental : rentalsTenant.getItem().get()) {
                rental.setTenantID("DeletedUser");
                upsert(rental);
            }
        }
    }
    public synchronized ServiceResponse<List<RentalDAO>> getRentalsByUserID(String userID) {
        CosmosPagedIterable<RentalDAO> response = container.queryItems("SELECT * FROM rental WHERE rental.tenantID=\"" + userID + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }

    public synchronized ServiceResponse<List<RentalDAO>>  getRentalsByHouseID(String houseID) {
        CosmosPagedIterable<RentalDAO> response = container.queryItems("SELECT * FROM rental WHERE rental.houseID=\"" + houseID + "\"", new CosmosQueryRequestOptions(), RentalDAO.class);

        return new ServiceResponse<>(200, response.stream().collect(Collectors.toList()));
    }
}
