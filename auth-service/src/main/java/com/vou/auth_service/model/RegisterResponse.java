package com.vou.auth_service.model;

public class RegisterResponse {
    private String message;

    // Constructors
    public RegisterResponse(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
