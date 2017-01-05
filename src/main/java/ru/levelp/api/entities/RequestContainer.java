package ru.levelp.api.entities;

/**
 * Created by кайрат on 01.01.2017.
 */
public class RequestContainer<T> extends BaseRequest {
    private T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
