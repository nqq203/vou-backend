package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuizWinnerRequest {
    private Long idEvent;

    private Long idGame;

    private Long userId;

    private Integer rank;
}
