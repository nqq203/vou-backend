package com.vou.auth_service.entity;

public class VerifyOtpResponse {
    String message;

    public VerifyOtpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
