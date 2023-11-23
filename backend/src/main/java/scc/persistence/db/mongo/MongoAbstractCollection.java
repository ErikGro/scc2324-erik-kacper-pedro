package scc.persistence.db.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import scc.cache.ServiceResponse;
import scc.persistence.db.Container;

public abstract class MongoAbstractCollection<T extends Document> implements Container<T> {
    protected MongoCollection<T> collection;

    MongoAbstractCollection(MongoCollection<T> collection) {
        this.collection = collection;
    }
    @Override
    public ServiceResponse<T> getByID(String id) {
//        Document doc = collection.find(eq("title", "Back to the Future")).first();
        return null;
    }

    @Override
    public ServiceResponse<T> upsert(T t) {
        return null;
    }

    @Override
    public ServiceResponse<Object> deleteByID(String id) {
        return null;
    }
}