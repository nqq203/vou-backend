package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionCorrectRates {
    private Long questionId;
    private Double rates;

    public QuestionCorrectRates(Long questionId, Double rates) {
        this.questionId = questionId;
        this.rates = rates;
    }
}
