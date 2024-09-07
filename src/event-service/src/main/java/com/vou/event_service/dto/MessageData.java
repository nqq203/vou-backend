package com.vou.event_service.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageData {
    private Long idEvent;
    private String imageUrl;
    private String message;
    private Integer date;

    public MessageData(Long idEvent, String imageUrl, String message, Integer date) {
        this.idEvent = idEvent;
        this.imageUrl = imageUrl;
        this.message = message;
        this.date = date;
    }
}
