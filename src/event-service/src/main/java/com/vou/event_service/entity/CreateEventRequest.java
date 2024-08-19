package com.vou.event_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CreateEventRequest {
    private String eventName;

    private String imageUrl;

    private Integer numberOfVouchers;

    private Timestamp startDate;

    private Timestamp endDate;

    public CreateEventRequest(String eventName, String imageUrl, Integer numberOfVouchers, Timestamp startDate, Timestamp endDate) {
        this.eventName = eventName;
        this.imageUrl = imageUrl;
        this.numberOfVouchers = numberOfVouchers;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
