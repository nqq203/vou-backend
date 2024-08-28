package com.vou.streaming_service.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Table(name = "game")
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game")
    private Long idGame;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    // TYPE = 'quiz', 'shake', 'other', see hackmd for more in4
    @Column(name = "type")
    private String type;

    @Column(name = "event_id")
    private Long idEvent;

    @Column(name = "instruction")
    private String instruction;

    @Column(name = "started_at")
    private Timestamp startedAt;

    public Game(String name, String type, Long idEvent) {
        this.name = name;
        this.type = type;
        this.idEvent = idEvent;
    }

    public Game() {
    }
}
