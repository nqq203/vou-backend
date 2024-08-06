package com.vou.auth_service.entity;

public class ResendOtpRequest {
    private String username;

    public ResendOtpRequest() {

    }

    public ResendOtpRequest(String username, String email) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
