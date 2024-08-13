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

}
