package com.vou.auth_service.model;

import com.vou.auth_service.constant.Status;
import jakarta.persistence.*;

@Entity
@Table(name = "brands")
public class Brand extends User {
    @Column(name = "brand_name")
    private String brandName;
    @Column(name = "field")
    private String field;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Brand(User user, String password, Status status) {
        this.setUsername(user.getUsername());
        this.setPassword(password);
        this.setFullName(user.getFullName());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setLockedDate(user.getLockedDate());
        this.setRole(user.getRole());
        this.status = status;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getField() {
        return field;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
