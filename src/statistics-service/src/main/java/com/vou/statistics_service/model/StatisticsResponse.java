package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsResponse {
    private Long participants;

    public StatisticsResponse(Long participants) {
        this.participants = participants;
    }
}
