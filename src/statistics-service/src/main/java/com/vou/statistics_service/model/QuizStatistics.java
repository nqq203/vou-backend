package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuizStatistics extends StatisticsResponse{
    private List<QuizWinnerMetadata> winners;
    private List<QuestionCorrectRates> correctRates;

    public QuizStatistics(Long participants) {
        super(participants);
    }

    public QuizStatistics() {
        super(0L);
        winners = new ArrayList<>();
        correctRates = new ArrayList<>();
    }
}
