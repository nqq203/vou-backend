package com.vou.streaming_service.model;

public class PlayerResult {
    private Long idEvent;
    private String playerUsername;
    private Integer rank;

    public PlayerResult(Long idEvent, String playerUsername, Integer rank) {
        this.idEvent = idEvent;
        this.playerUsername = playerUsername;
        this.rank = rank;
    }
}
