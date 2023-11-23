package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import scc.cache.Identifiable;
import scc.cache.ServiceResponse;
import scc.persistence.db.Container;

import static com.mongodb.client.model.Filters.eq;

public abstract class MongoAbstractCollection<T extends Document & Identifiable> implements Container<T> {
    protected MongoCollection<T> collection;

    MongoAbstractCollection(MongoCollection<T> collection) {
        this.collection = collection;
    }
    @Override
    public synchronized ServiceResponse<T> getByID(String id) {
        return getResponse(collection.find(eq("id", id)).first());
    }

    @Override
    public synchronized ServiceResponse<T> upsert(T t) {
        return getResponse(collection.findOneAndReplace(eq("id", t.getId()), t));
    }

    @Override
    public synchronized ServiceResponse<Object> deleteByID(String id) {
        collection.findOneAndDelete(eq("id", id));
        // TODO: check
        return new ServiceResponse<>(200);
    }

    public synchronized ServiceResponse<T> getResponse(T returnVal) {
        if (returnVal == null) {
            return new ServiceResponse<>(404);
        } else {
            return new ServiceResponse<>(200, returnVal);
        }
    }
}