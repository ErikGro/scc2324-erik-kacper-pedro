package scc.db;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;

public abstract class AbstractDB<T> {
    protected CosmosContainer container;
    private final Class<T> type;

    public AbstractDB(CosmosContainer container, Class<T> type) {
        this.container = container;
        this.type = type;
    }

    public CosmosItemResponse<T> getByID(String id) {
        return container.readItem(id, new PartitionKey(id), type);
    }

    public CosmosItemResponse<T> upsert(T t) {
        return container.upsertItem(t);
    }

    public CosmosItemResponse<Object> deleteByID(String id) {
        return container.deleteItem(id, new PartitionKey(id), new CosmosItemRequestOptions());
    }
}