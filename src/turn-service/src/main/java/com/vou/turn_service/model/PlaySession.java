package com.vou.turn_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaySession {
    Long idPlaySession;
    Long idPlayer;
    Long idGame;
    int score;
    int turns;

    public PlaySession(Long idPlayer, Long idGame, int score, int turns) {
        this.idGame = idGame;
        this.idPlayer = idPlayer;
        this.score = score;
        this.turns = turns;
    }
}
