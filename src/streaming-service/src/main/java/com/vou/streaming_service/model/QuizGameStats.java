package com.vou.streaming_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizGameStats {
    private Long idQuizGameStat;

    private Long idEvent;

    private Long idGame;

    private Long numberOfParticipants;
}
