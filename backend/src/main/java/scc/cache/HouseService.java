package scc.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import redis.clients.jedis.Jedis;
import scc.data.house.HouseDAO;
import scc.persistence.db.cosmos.CosmosDBLayer;
import scc.persistence.db.cosmos.CosmosHouseDB;

import java.util.Collections;
import java.util.List;

public class HouseService extends AbstractService<HouseDAO, CosmosHouseDB> {
    public HouseService() {
        super(HouseDAO.class, "house:", CosmosDBLayer.getInstance().getHouseDB());
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }

    public List<HouseDAO> getDiscountedSoon() {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            String cacheValue = jedis.get("discountedNearFuture");

            return mapper.readValue(cacheValue, new TypeReference<>() {});
        } catch (Exception ignored) {
            // ignore
        }

        ServiceResponse<List<HouseDAO>> houses = db.getDiscountedHousesNearFuture();

        if (houses.getItem().isEmpty())
            return Collections.emptyList();

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set("discountedNearFuture", mapper.writeValueAsString(houses));
        } catch (Exception ignored) {
            // ignore
        }

        return houses.getItem().get();
    }
}
