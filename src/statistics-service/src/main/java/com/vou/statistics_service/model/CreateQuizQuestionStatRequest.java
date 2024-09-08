package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuizQuestionStatRequest {
    Long idQuizQuestion;
    Long idGame;
    Long idEvent;
    Boolean result;
}
