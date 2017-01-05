package ru.levelp.api.entities;

/**
 * Created by кайрат on 01.01.2017.
 */
public class ResponseContainer<T> {
    private String requestId;
    private int code;
    private T payload;

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
