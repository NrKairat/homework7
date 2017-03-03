package ru.levelp.api.entities;

import com.google.gson.annotations.Expose;

/**
 * Created by кайрат on 01.01.2017.
 */
public class RequestContainer<T> extends BaseRequest {
    @Expose
    private T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
