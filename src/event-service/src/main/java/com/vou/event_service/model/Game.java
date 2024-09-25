package com.vou.event_service.model;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class Game {
    private Long idGame;
    private String name;
    private String imageUrl;
    private String type;
    private String instruction;
    private Timestamp startedAt;
}
