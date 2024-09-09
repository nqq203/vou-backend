package com.vou.streaming_service.model;

public class PlayerResult {
    private Long idEvent;
    private Long playerId;
    private Integer rank;

    public PlayerResult(Long idEvent, Long playerId, Integer rank) {
        this.idEvent = idEvent;
        this.playerId = playerId;
        this.rank = rank;
    }
}
