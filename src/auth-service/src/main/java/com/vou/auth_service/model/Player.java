package com.vou.auth_service.model;

import com.vou.auth_service.constant.Gender;
import jakarta.persistence.*;

public class Player extends User {
    private String avatarUrl;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String facebookUrl;
    public Player(User user, String password) {
        super();
        this.setUsername(user.getUsername());
        this.setPassword(password);
        this.setFullName(user.getFullName());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setLockedDate(user.getLockedDate());
        this.setRole(user.getRole());
        this.setStatus(user.getStatus());
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }
}
