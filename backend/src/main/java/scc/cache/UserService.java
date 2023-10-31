package scc.cache;

import scc.data.UserDAO;
import scc.db.CosmosDBLayer;
import scc.db.UserDB;

public class UserService extends AbstractService<UserDAO, UserDB> {
    private final HouseService houseService = new HouseService();
    private final RentalService rentalService = new RentalService();

    public UserService() {
        super(UserDAO.class, "user:", CosmosDBLayer.getInstance().userDB);
    }

    @Override public ServiceResponse<UserDAO> deleteByID(String id) {
        ServiceResponse<UserDAO> response = super.deleteByID(id);

        // For all houses and rentals assosiated with user set userId to "DeletedUser"
        houseService.deleteUserID(id);
        rentalService.deleteUserID(id);

        return response;
    }
    
}
