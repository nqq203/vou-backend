package com.vou.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player")
    private Long idPlayer;

    @Column(name = "avatar_uri")
    private String avatarUri;

    @Column(name = "facebook")
    private String facebook;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_player", unique = true)
    private User user;
}
