package com.vou.user_service.model;

import com.vou.user_service.constant.Gender;
import com.vou.user_service.constant.Role;
import com.vou.user_service.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Player extends User {
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "facebook_url")
    private String facebookUrl;

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
