package scc.cache;

import java.util.Optional;

public class ServiceResponse<T> {
    private int statusCode;
    private T item;

    public ServiceResponse(int statusCode, T item) {
        this.statusCode = statusCode;
        this.item = item;
    }

    public ServiceResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Optional<T> getItem() {
        return Optional.ofNullable(item);
    }
}
