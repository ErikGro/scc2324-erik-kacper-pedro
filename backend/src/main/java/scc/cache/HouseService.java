package scc.cache;

import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.core.type.TypeReference;
import redis.clients.jedis.Jedis;
import scc.data.house.HouseDAO;
import scc.db.CosmosDBLayer;
import scc.db.HouseDB;

import java.util.Set;
import java.util.stream.Collectors;

public class HouseService extends AbstractService<HouseDAO, HouseDB> {
    public HouseService() {
        super(HouseDAO.class, "house:", CosmosDBLayer.getInstance().getHouseDB());
    }

    public void deleteUserID(String id) {
        db.deleteUserID(id);
    }

    public Set<HouseDAO> getDiscountedSoon() {
        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            String cacheValue = jedis.get("discountedNearFuture");

            return mapper.readValue(cacheValue, new TypeReference<>() {});
        } catch (Exception ignored) {
            // ignore
        }

        CosmosPagedIterable<HouseDAO> houses = db.getDiscountedHousesNearFuture();
        Set<HouseDAO> set = houses.stream().collect(Collectors.toSet());

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set("discountedNearFurture", mapper.writeValueAsString(set));
        } catch (Exception ignored) {
            // ignore
        }

        return set;
    }
}
