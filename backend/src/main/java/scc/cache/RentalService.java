package scc.cache;

import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.RentalDAO;
import scc.persistence.db.CosmosDBLayer;
import scc.persistence.db.RentalDB;
import java.util.Set;
import java.util.stream.Collectors;

public class RentalService extends AbstractService<RentalDAO, RentalDB> {
    public RentalService() {
        super(RentalDAO.class, "rental:", CosmosDBLayer.getInstance().getRentalDB());
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }

    public ServiceResponse<Set<RentalDAO>> getRentalsForHouse(String houseID) {
        CosmosPagedIterable<RentalDAO> response = db.getRentalsByHouseID(houseID);

        if (!response.iterator().hasNext()) {
            return new ServiceResponse<>(400, null);
        } else {
            return new ServiceResponse<>(200, response.stream().collect(Collectors.toSet()));
        }
    }
}
