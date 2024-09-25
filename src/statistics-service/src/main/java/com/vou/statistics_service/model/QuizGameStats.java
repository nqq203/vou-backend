package com.vou.statistics_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quiz_game_stats")
public class QuizGameStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quiz_game_stat")
    private Long idQuizGameStat;

    @Column(name = "id_event")
    private Long idEvent;

    @Column(name = "number_of_participants")
    private Long numberOfParticipants;

    public QuizGameStats(Long idEvent, Long numberOfParticipants) {
        this.idEvent = idEvent;
        this.numberOfParticipants = numberOfParticipants;
    }

    public QuizGameStats() {

    }
}
