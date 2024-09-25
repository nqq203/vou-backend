package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Game {
    private Long idGame;

    private String name;

    private String imageUrl;

    // TYPE = 'quiz', 'shake', 'other', see hackmd for more in4
    private String type;

    private Long idEvent;

    private String instruction;

    private Timestamp startedAt;
}
