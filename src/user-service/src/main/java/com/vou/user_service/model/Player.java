package com.vou.user_service.model;

import com.vou.user_service.constant.Gender;
import com.vou.user_service.constant.Role;
import com.vou.user_service.constant.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Player extends User {
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "facebook_url")
    private String facebookUrl;
}
