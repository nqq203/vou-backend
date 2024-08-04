package com.vou.user_service.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {
    private boolean success = true;
    private String message;
    private int code;
    private Object metadata;

    public SuccessResponse() {}

    public SuccessResponse(String message, HttpStatus status, Object metadata) {
        this.message = message;
        this.code = status.value();
        this.metadata = metadata;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}

