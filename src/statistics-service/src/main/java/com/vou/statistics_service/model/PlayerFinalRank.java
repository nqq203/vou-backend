package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerFinalRank {
    private Long idEvent;

    private String playerUsername;

    private Integer rank;
}
