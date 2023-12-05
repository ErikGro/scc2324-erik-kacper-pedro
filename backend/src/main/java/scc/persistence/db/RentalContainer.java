package scc.persistence.db;

import scc.cache.ServiceResponse;
import scc.data.RentalDAO;

import java.util.List;

public interface RentalContainer extends Container<RentalDAO> {
    void deleteUserID(String id);
    ServiceResponse<List<RentalDAO>> getRentalsByUserID(String userID);
    ServiceResponse<List<RentalDAO>> getRentalsByHouseID(String houseID);
}
