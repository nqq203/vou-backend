package com.vou.reward_service.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateItemRequest {
    String itemName;

    String description;

    String imageUrl;

    Long idEvent;
}
