package com.vou.statistics_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quiz_winners")
public class QuizWinner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quiz_winner")
    private Long idQuizWinner;

    @Column(name = "id_event")
    private Long idEvent;

    @Column(name = "id_game")
    private Long idGame;

    @Column(name = "winner_id")
    private Long winnerId;

    @Column(name = "rank")
    private Integer rank;

    public QuizWinner() {}

    public QuizWinner(Long idEvent, Long idGame, Integer rank, Long winnerId) {
        this.idEvent = idEvent;
        this.idGame = idGame;
        this.rank = rank;
        this.winnerId = winnerId;
    }
}
