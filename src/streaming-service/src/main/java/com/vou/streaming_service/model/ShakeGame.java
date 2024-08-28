package com.vou.streaming_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ShakeGame {
    @Id
    private Long id_game;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_game")
    private Game game;

    public ShakeGame() {
    }
}
