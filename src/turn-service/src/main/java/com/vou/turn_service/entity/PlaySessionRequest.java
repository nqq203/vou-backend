package com.vou.turn_service.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaySessionRequest {
    Long idPlayer;
    Long idGame;
    int score;
    int turns;
}
