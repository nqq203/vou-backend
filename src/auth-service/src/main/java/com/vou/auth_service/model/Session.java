package com.vou.auth_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Session {
    private Long id;
    private String token;
    private boolean isActive;

    @Column
    private LocalDateTime logoutAt;

    private LocalDateTime expiration;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(LocalDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
