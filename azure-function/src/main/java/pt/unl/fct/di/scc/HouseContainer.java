    package pt.unl.fct.di.scc;


import java.util.List;

public interface HouseContainer extends Container<HouseDAO> {
    void deleteUserID(String id);
    ServiceResponse<List<HouseDAO>> getHousesByUserID(String id);
    ServiceResponse<List<HouseDAO>> getHousesByCity(String name);
    ServiceResponse<List<HouseDAO>> getHousesByCityAndPeriod(String name, String startDate, String endDate);
    ServiceResponse<List<HouseDAO>> getDiscountedHousesNearFuture();
}
