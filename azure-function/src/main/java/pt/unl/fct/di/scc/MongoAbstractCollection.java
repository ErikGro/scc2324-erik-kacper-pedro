package pt.unl.fct.di.scc;

import dev.morphia.Datastore;

import static dev.morphia.query.filters.Filters.eq;

public abstract class MongoAbstractCollection<T extends Identifiablei> implements Container<T> {
    private final Class<T> type;
    final Datastore datastore;

    MongoAbstractCollection(Class<T> type, Datastore datastore) {
        this.type = type;
        this.datastore = datastore;
    }

    @Override
    public synchronized ServiceResponse<T> getByID(String id) {
        T item = datastore.find(type)
                .filter(eq("id", id))
                .first();

        if (item == null) {
            return new ServiceResponse<>(404);
        } else {
            return new ServiceResponse<T>(200, item);
        }
    }

    @Override
    public synchronized ServiceResponse<T> upsert(T t) {
        deleteByID(t.getId());
        datastore.insert(t);

        return new ServiceResponse<>(201, t);
    }

    @Override
    public synchronized ServiceResponse<Object> deleteByID(String id) {
        datastore.find(type)
                .filter(eq("id", id))
                .delete();

        return new ServiceResponse<>(200);
    }
}