package scc.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import scc.persistence.db.Container;
import scc.utils.Constants;

import java.util.Optional;

public abstract class AbstractService<T extends Identifiable, C extends Container<T>> {
    protected final C container;
    private final Class<T> type;
    protected final ObjectMapper mapper = new ObjectMapper();
    private final String cachingPrefix;

    public AbstractService(Class<T> type, String cachingPrefix, C container) {
        this.type = type;
        this.cachingPrefix = cachingPrefix;
        this.container = container;
    }

    public ServiceResponse<T> getByID(String id) {
        Optional<T> cache = getFromCacheByID(id);
        if (cache.isPresent()) { // Cache hit
            return new ServiceResponse<>(200, cache.get());
        }

        // Cache miss
        ServiceResponse<T> response = container.getByID(id);
        Optional<T> item = response.getItem();

        // Cache item
        item.ifPresent(this::writeToCache);

        return response;
    }

    public ServiceResponse<T> upsert(T object) {
        ServiceResponse<T> response = container.upsert(object);

        if (response.getStatusCode() < 300 && response.getItem().isPresent()) {
            writeToCache(response.getItem().get());
        }

        return response;
    }

    public ServiceResponse<Object> deleteByID(String id) {
        ServiceResponse<Object> response = container.deleteByID(id);
        deleteFromCache(id);

        return response;
    }

    /////////////////// CACHING METHODS ///////////////////////

    protected void writeToCache(T object) {
        if (!Constants.cachingEnabled) return;

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set(cachingPrefix + object.getId(), mapper.writeValueAsString(object));
        } catch (Exception ignored) {
            // Do nothing
        }
    }

    protected void writeToCache(T object, String cacheID) {
        if (!Constants.cachingEnabled) return;

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.set(cachingPrefix + cacheID, mapper.writeValueAsString(object));
        } catch (Exception ignored) {
            // Do nothing
        }
    }

    protected void deleteFromCache(String cacheID) {
        if (!Constants.cachingEnabled) return;

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            jedis.del(cachingPrefix + cacheID);
        }
    }

    protected Optional<T> getFromCacheByID(String cacheID) {
        if (!Constants.cachingEnabled) return Optional.empty();

        try (Jedis jedis = RedisCache.getCachePool().getResource()) {
            String cacheValue = jedis.get(cachingPrefix + cacheID);
            T object = mapper.readValue(cacheValue, type);

            return Optional.of(object);
        } catch (Exception ignored) {
            // Return empty optional
        }

        return Optional.empty();
    }
}
