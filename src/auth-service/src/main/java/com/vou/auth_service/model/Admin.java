package com.vou.auth_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends User {

    public Admin(User user, String password) {
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
}
