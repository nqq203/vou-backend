package com.vou.event_service.dto;

import com.vou.event_service.model.BrandsCooperation;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class EventDetailDTO {
    private Long idEvent;
    private String eventName;
    private int numberOfVouchers;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<BrandsCooperation> brands;
    private GameInfoDTO gameInfoDTO;
    private InventoryDetailDTO inventoryDetailDTO;

    public EventDetailDTO(
            Long idEvent,
            String eventName,
            int numberOfVouchers,
            Timestamp startDate,
            Timestamp endDate,
            List<BrandsCooperation> brands,
            GameInfoDTO gameInfoDTO,
            InventoryDetailDTO inventoryDetailDTO
    ) {
        this.idEvent = idEvent;
        this.eventName = eventName;
        this.numberOfVouchers = numberOfVouchers;
        this.startDate = startDate;
        this.endDate = endDate;
        this.brands = brands;
        this.gameInfoDTO = gameInfoDTO;
        this.inventoryDetailDTO = inventoryDetailDTO;
    }
}
