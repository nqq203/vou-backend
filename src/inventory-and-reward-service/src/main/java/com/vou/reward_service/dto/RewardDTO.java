package com.vou.reward_service.dto;

import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.ItemRepo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardDTO {
    Long idItemRepo;
    Long idPlayer;
    Long idItem;
    Long amount;
    String itemName;
    String imageUrl;

    public RewardDTO(Item item, ItemRepo itemRepo) {
        this.idItemRepo = itemRepo.getIdItemRepo();
        this.idItem = item.getIdItem();
        this.idPlayer = itemRepo.getIdPlayer();
        this.amount = itemRepo.getAmount();
        this.itemName = item.getItemName();
        this.imageUrl = item.getImageUrl();
    }
}
