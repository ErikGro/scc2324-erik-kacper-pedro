    package pt.unl.fct.di.scc;


import java.util.List;

public interface RentalContainer extends Container<RentalDAO> {
    void deleteUserID(String id);
    ServiceResponse<List<RentalDAO>> getRentalsByUserID(String userID);
    ServiceResponse<List<RentalDAO>> getRentalsByHouseID(String houseID);
}
