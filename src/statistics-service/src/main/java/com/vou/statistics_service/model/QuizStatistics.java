package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizStatistics extends StatisticsResponse{
    private List<User> winners;

    public QuizStatistics(Long participants) {
        super(participants);
    }
}
