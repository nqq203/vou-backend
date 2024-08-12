package com.vou.auth_service.entity;

public class LoginResponse {
    private String token;
    private Long idUser;

    // Constructors, Getters and Setters
    public LoginResponse(String token, Long idUser) {
        this.token = token;
        this.idUser = idUser;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
