package com.vou.auth_service.entity;

import com.vou.auth_service.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Object account;

    // Constructors, Getters and Setters
    public LoginResponse(String token, Object account) {
        this.token = token;
        this.account = account;
    }

    public LoginResponse() {
    }


}
