package com.vou.streaming_service.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Notification {
    private Long idEvent;
    private String imageUrl;
    private String message;
    private Integer numDay;
}
