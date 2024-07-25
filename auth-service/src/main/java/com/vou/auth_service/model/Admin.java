package com.vou.auth_service.model;

import com.vou.auth_service.constant.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
    @Enumerated(EnumType.STRING)
    private Status status;

    public Admin(User user, String password, Status status) {
        this.setUsername(user.getUsername());
        this.setPassword(password);
        this.setFullName(user.getFullName());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setLockedDate(user.getLockedDate());
        this.setRole(user.getRole());
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
