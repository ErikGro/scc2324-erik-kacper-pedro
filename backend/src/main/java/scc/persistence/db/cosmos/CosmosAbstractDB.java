package scc.persistence.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;
import scc.cache.ServiceResponse;
import scc.persistence.db.DB;

public abstract class CosmosAbstractDB<T> implements DB<T> {
    protected final CosmosContainer container;
    private final Class<T> type;

    protected CosmosAbstractDB(CosmosContainer container, Class<T> type) {
        this.container = container;
        this.type = type;
    }

    public synchronized ServiceResponse<T> getByID(String id) {
        CosmosItemResponse<T> response = container.readItem(id, new PartitionKey(id), type);
        return new ServiceResponse<>(response.getStatusCode(), response.getItem());
    }

    public synchronized ServiceResponse<T> upsert(T t) {
        CosmosItemResponse<T> response = container.upsertItem(t);
        return new ServiceResponse<>(response.getStatusCode(), response.getItem());
    }

    public synchronized ServiceResponse<Object> deleteByID(String id) {
        CosmosItemResponse<Object> response = container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
        return new ServiceResponse<>(response.getStatusCode(), response.getItem());
    }
}
