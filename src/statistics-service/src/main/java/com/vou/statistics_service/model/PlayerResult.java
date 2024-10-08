package com.vou.statistics_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "player_result")
public class PlayerResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_player_result")
    private Long idPlayerResult;

    @Column(name = "id_event")
    private Long idEvent;

    @Column(name = "player_username")
    private String playerUsername;

    @Column(name = "rank")
    private Integer rank;

    public PlayerResult() {}

    public PlayerResult(Long idEvent, Integer rank, String playerUsername) {
        this.idEvent = idEvent;
        this.rank = rank;
        this.playerUsername = playerUsername;
    }
}
