package com.vou.auth_service.model;

public class LoginResponse {
    private String token;

    // Constructors, Getters and Setters
    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
