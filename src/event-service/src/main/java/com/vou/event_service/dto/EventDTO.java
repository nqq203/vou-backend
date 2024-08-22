package com.vou.event_service.dto;

import com.vou.event_service.model.Game;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String eventName;
    private String imageUrl;

    private int numberOfVouchers;
    private Timestamp startDate;
    private Timestamp endDate;
    private int brandId;
    private String gameType;
    private GameInfoDTO gameInfoDTO;

}