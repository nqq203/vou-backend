package com.vou.reward_service.dto;

import com.vou.reward_service.model.Item;
import lombok.Data;

@Data
public class ItemDetailDTO {
    private Long idItem;
    private String description;
    private Long idEvent;
    private String imageUrl;
    private String itemName;

    public ItemDetailDTO(Item item) {
        this.idItem = item.getIdItem();
        this.description = item.getDescription();
        this.idEvent = item.getIdEvent();
        this.imageUrl = item.getImageUrl();
        this.itemName = item.getItemName();
    }

}
