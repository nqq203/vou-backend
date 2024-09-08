package com.vou.statistics_service.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuizGameStatRequest {
    private Long idEvent;

    private Long idGame;
}
