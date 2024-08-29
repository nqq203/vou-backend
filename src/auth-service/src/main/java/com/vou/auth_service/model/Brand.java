package com.vou.auth_service.model;

public class Brand extends User {
    private String logo_url;
    private String field;
    private double latitude;
    private double longitude;

    public Brand(User user, String password) {
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

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
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


    public void setField(String field) {
        this.field = field;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
