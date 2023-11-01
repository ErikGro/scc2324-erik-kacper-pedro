package scc.cache;

import com.azure.cosmos.util.CosmosPagedIterable;
import redis.clients.jedis.Jedis;
import scc.data.UserDAO;
import scc.db.CosmosDBLayer;
import scc.db.UserDB;

import java.util.Optional;

public class UserService extends AbstractService<UserDAO, UserDB> {
    private final HouseService houseService = new HouseService();
    private final RentalService rentalService = new RentalService();

    public UserService() {
        super(UserDAO.class, "user:", CosmosDBLayer.getInstance().userDB);
    }

    public ServiceResponse<UserDAO> getByUsername(String username) {
        CosmosPagedIterable<UserDAO> response = db.getByUsername(username);

        if (!response.iterator().hasNext()) {
            return new ServiceResponse<>(404, null);
        }

        return new ServiceResponse<>(200, response.iterator().next());
    }

    @Override
    public ServiceResponse<UserDAO> deleteByID(String id) {
        ServiceResponse<UserDAO> response = super.deleteByID(id);

        // For all houses and rentals assosiated with user set userId to "DeletedUser"
        houseService.deleteUserID(id);
        rentalService.deleteUserID(id);

        return response;
    }

    ////////////////// SESSION HANDLING //////////////////

    public void putSession(String sessionID, String userID) {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set("session:" + sessionID, userID);
        }
    }

    public Optional<String> getUserIDBySession(String sessionID) {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            return Optional.ofNullable(jedis.get("session:" + sessionID));
        }
    }

    public boolean userSessionInvalid(String sessionID, String userID) {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            String sessionUserID = jedis.get("session:" + sessionID);
            return !userID.equals(sessionUserID);
        }
    }
}
