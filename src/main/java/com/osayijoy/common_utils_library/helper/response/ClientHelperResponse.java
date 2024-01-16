package com.osayijoy.common_utils_library.helper.response;

import java.util.List;
import org.springframework.http.HttpStatus;

public class ClientHelperResponse<T> {
    private T response;
    private boolean success;
    private HttpStatus responseStatus;
    private String responseString;

    public ClientHelperResponse(T response, HttpStatus responseStatus, List<Object> errors, boolean success) {
        this.success = success;
        this.response = response;
        this.responseStatus = responseStatus;
    }

    public ClientHelperResponse(T response) {
        this(response, HttpStatus.OK);
        this.response = response;
        this.success = true;
    }

    public ClientHelperResponse(T response, HttpStatus responseStatus) {
        this();
        this.responseStatus = responseStatus;
    }

    public ClientHelperResponse() {
        this.responseStatus = HttpStatus.OK;
        this.success = true;
    }

    public HttpStatus getResponseStatus() {
        return this.responseStatus;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public T getResponse() {
        return this.response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponseString() {
        return this.responseString;
    }

    public ClientHelperResponse setResponseString(String responseString) {
        this.responseString = responseString;
        return this;
    }
}
