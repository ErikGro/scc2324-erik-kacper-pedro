package scc.persistence.db;

import scc.cache.ServiceResponse;

public interface DB<T> {
    ServiceResponse<T> getByID(String id);

    ServiceResponse<T> upsert(T t);

    ServiceResponse<Object> deleteByID(String id);
}
