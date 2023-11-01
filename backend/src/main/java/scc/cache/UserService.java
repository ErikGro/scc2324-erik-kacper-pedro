package scc.cache;

import com.azure.cosmos.util.CosmosPagedIterable;
import redis.clients.jedis.Jedis;
import scc.data.UserDAO;
import scc.db.CosmosDBLayer;
import scc.db.UserDB;

public class UserService extends AbstractService<UserDAO, UserDB> {
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

    public void putSession(String sessionID, String userID) {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set("session:" + sessionID, userID);
        }
    }

    public boolean userSessionInvalid(String sessionID, String userID) {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            String sessionUserID = jedis.get("session:" + sessionID);
            return !userID.equals(sessionUserID);
        }
    }
}
