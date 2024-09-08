package com.vou.streaming_service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class EventDTO {
    private Long idEvent;

    private String eventName;

    private String imageUrl;

    private Integer numberOfVouchers;

    private Timestamp startDate;

    private Timestamp endDate;

    private Timestamp deletedDate;

    private Long createdBy;
}

