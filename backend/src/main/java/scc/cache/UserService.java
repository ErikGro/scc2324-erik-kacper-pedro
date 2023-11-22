package scc.cache;

import redis.clients.jedis.Jedis;
import scc.data.UserDAO;
import scc.persistence.db.cosmos.CosmosDBLayer;
import scc.persistence.db.cosmos.CosmosUserDB;
import scc.utils.Constants;

import java.util.List;
import java.util.Optional;

public class UserService extends AbstractService<UserDAO, CosmosUserDB> {
    private final HouseService houseService = new HouseService();
    private final RentalService rentalService = new RentalService();

    public UserService() {
        super(UserDAO.class, "user:", CosmosDBLayer.getInstance().getUserDB());
    }

    public ServiceResponse<UserDAO> getByUsername(String username) {
        if (Constants.cachingEnabled) {
            Optional<UserDAO> cache = getFromCacheByID(username);
            if (cache.isPresent()) { // Cache hit
                return new ServiceResponse<>(200, cache.get());
            }
        }

        // Cache miss
        ServiceResponse<List<UserDAO>> response = db.getByUsername(username);
        if (response.getItem().isEmpty()) {
            return new ServiceResponse<>(404, null);
        }

        UserDAO user = response.getItem().get().get(0);

        // Cache item
        if (Constants.cachingEnabled) {
            writeToCache(user, username);
        }

        return new ServiceResponse<>(200, user);
    }

    @Override
    public ServiceResponse<Object> deleteByID(String id) {
        ServiceResponse<Object> response = super.deleteByID(id);

        // For all houses and rentals associated with user set userId to "DeletedUser"
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
