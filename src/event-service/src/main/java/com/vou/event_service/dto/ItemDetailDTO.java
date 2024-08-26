package com.vou.event_service.dto;

import lombok.Data;

@Data
public class ItemDetailDTO {
    private Long idItem;
    private String description;
    private Long idEvent;
    private String imageUrl;
    private String itemName;
}
