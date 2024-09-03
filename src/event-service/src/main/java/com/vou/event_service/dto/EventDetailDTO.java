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
    private String imageUrl;
    private String brandLogo;
    private Long createdBy;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<BrandsCooperation> brandId;
    private GameInfoDTO gameInfoDTO;
    private InventoryDetailDTO inventoryInfo;
    private Long turns;

    public EventDetailDTO(
            Long idEvent,
            String eventName,
            int numberOfVouchers,
            String imageUrl,
            String brandLogo,
            Long createdBy,
            Timestamp startDate,
            Timestamp endDate,
            List<BrandsCooperation> brands,
            GameInfoDTO gameInfoDTO,
            InventoryDetailDTO inventoryDetailDTO,
            Long turns
    ) {
        this.idEvent = idEvent;
        this.eventName = eventName;
        this.numberOfVouchers = numberOfVouchers;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.brandId = brands;
        this.gameInfoDTO = gameInfoDTO;
        this.inventoryInfo = inventoryDetailDTO;
        this.createdBy = createdBy;
        this.brandLogo = brandLogo;
        this.turns = turns;
    }
}
