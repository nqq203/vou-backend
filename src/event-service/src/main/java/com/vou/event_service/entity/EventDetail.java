package com.vou.event_service.entity;

import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.InventoryDetailDTO;
import com.vou.event_service.model.BrandsCooperation;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class EventDetail {
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
    private String instruction;

    public EventDetail(
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
            String instruction
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
        this.instruction = instruction;
    }
}
