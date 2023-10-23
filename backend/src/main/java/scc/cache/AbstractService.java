package scc.cache;

import com.azure.cosmos.models.CosmosItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import scc.db.AbstractDB;
import java.util.Optional;

public class AbstractService <T extends Cachable, DBType extends AbstractDB<T>> {
    protected final DBType db;
    private final Class<T> type;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String cachingPrefix;

    public AbstractService(Class<T> type, String cachingPrefix, DBType db) {
        this.type = type;
        this.cachingPrefix = cachingPrefix;
        this.db = db;
    }

    public ServiceResponse<T> getByID(String id) {
        Optional<T> cache = getFromCacheByID(id);
        if (cache.isPresent()) { // Cache hit
            return new ServiceResponse<>(200, cache.get());
        }

        // Cache miss
        CosmosItemResponse<T> response = db.getByID(id);
        T item = response.getItem();

        if (item != null) { // Cache item
            writeToCache(item);
        }

        return new ServiceResponse<>(response.getStatusCode(), item);
    }

    public ServiceResponse<T> upsert(T object) {
        CosmosItemResponse<T> response = db.upsert(object);

        if (response.getStatusCode() < 300) {
            writeToCache(object);
        }

        return new ServiceResponse<>(response.getStatusCode(), response.getItem());
    }

    public ServiceResponse<T> deleteByID(String id) {
        CosmosItemResponse<Object> response = db.deleteByID(id);
        deleteFromCache(id);

        return new ServiceResponse<>(response.getStatusCode());
    }

    protected void writeToCache(T object) {
        try(Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set(cachingPrefix + object.getCachingKey(), mapper.writeValueAsString(object));
        } catch (JsonProcessingException ignored) {

        }
    }

    protected void deleteFromCache(String id) {
        try(Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.del(cachingPrefix + id);
        }
    }

    protected Optional<T> getFromCacheByID(String id) {
        try(Jedis jedis = RedisCache.getCachePool().getResource()) {
            String cacheValue = jedis.get(cachingPrefix + id);
            T object = mapper.readValue(cacheValue, type);

            return Optional.of(object);
        } catch (JsonProcessingException ignored) {

        }

        return Optional.empty();
    }
}
