package scc.persistence.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;

public abstract class AbstractDB<T> {
    protected final CosmosContainer container;
    private final Class<T> type;

    protected AbstractDB(CosmosContainer container, Class<T> type) {
        this.container = container;
        this.type = type;
    }

    public synchronized CosmosItemResponse<T> getByID(String id) {
        return container.readItem(id, new PartitionKey(id), type);
    }

    public synchronized CosmosItemResponse<T> upsert(T t) {
        return container.upsertItem(t);
    }

    public synchronized CosmosItemResponse<Object> deleteByID(String id) {
        return container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
    }
}
