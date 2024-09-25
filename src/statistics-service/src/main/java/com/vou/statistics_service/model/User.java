package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private Long idUser;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String phoneNumber;

    private LocalDateTime lockedDate;

    private String role;

    private String status;

    private String address;

    private String avatarUrl;

    public User(String username, String password, String fullName, String email, String phoneNumber, String role, String status) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
    }
}
