package com.vou.user_service.model;

import com.vou.user_service.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "brands")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Brand extends User {
    @Column(name = "logo_url")
    private String logo_url;
    @Column(name = "field")
    private String field;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;

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
