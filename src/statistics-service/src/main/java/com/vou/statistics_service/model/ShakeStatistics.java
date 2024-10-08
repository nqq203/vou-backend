package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShakeStatistics extends StatisticsResponse{
    private Long remainingVouchers;
    private Long givenVouchers;
    private Long shareCount;
    public ShakeStatistics(Long participants) {
        super(participants);
    }
}
