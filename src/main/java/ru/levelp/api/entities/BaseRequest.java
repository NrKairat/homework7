package ru.levelp.api.entities;

/**
 * Created by кайрат on 30.12.2016.
 */
public class BaseRequest {
    protected String requestId;
    protected String method;
    protected String token;


    public String getRequestId() {
        return requestId;
    }

    public String getMethod() {
        return method;
    }

    public String getToken() {
        return token;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
