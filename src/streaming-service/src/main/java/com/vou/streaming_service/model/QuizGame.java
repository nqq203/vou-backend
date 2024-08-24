package com.vou.streaming_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class QuizGame {
    @Id
    private Long id_game;

    private int aim_score;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_game")
    private Game game;

    public QuizGame(int aim_score) {
        this.aim_score = aim_score;
    }
}

