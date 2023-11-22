package scc.persistence.db;

import scc.cache.ServiceResponse;

import java.util.List;

public interface RentalDB<RentalDAO> extends DB<RentalDAO> {
    void deleteUserID(String id);
    ServiceResponse<List<RentalDAO>> getRentalsByUserID(String userID);
    ServiceResponse<List<RentalDAO>> getRentalsByHouseID(String houseID);
}
