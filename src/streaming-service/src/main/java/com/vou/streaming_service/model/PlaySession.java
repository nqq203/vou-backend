package com.vou.streaming_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "PlaySession")
@Entity
public class PlaySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_playsession")
    private Long idPlaySession;

    @Column(name = "id_player")
    private Long idPlayer;

    @Column(name = "id_game")
    private Long idGame;

    @Column(name = "score")
    private int score;

    @Column(name = "turns")
    private int turns;

    public PlaySession(Long idPlayer, Long idGame, int score, int turns) {
        this.idGame = idGame;
        this.idPlayer = idPlayer;
        this.score = score;
        this.turns = turns;
    }

    public PlaySession() {}
}
