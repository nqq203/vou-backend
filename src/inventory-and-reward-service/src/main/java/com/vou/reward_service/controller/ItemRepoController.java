package com.vou.reward_service.controller;

import com.vou.reward_service.dto.RewardDTO;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.ItemRepo;
import com.vou.reward_service.service.ItemRepoService;
import com.vou.reward_service.service.ItemService;
import com.vou.reward_service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/item-repos")
@CrossOrigin
public class ItemRepoController {
    @Autowired
    ItemRepoService itemRepoService;
    @Autowired
    ItemService itemService;
    @Autowired
    VoucherService voucherService;

    @GetMapping("/{id_user}")
    public ResponseEntity<?> getRewardsByIdUser(@PathVariable Long id_user) {
        try {
            List<ItemRepo> itemRepoList = itemRepoService.getItemRepoListByIdUser(id_user);
            List<RewardDTO> rewards = new ArrayList<>();

            for (ItemRepo itemRepo : itemRepoList) {
                Item item = itemService.getItemById(itemRepo.getIdItem());
                if (item != null) {
                    rewards.add(new RewardDTO(item, itemRepo));
                }
            }

            return ResponseEntity.ok(rewards);
        } catch (Exception e) {
            System.out.println("Error in getRewardsByIdUser: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id_item_repo}")
    public ResponseEntity<RewardDTO> incrementAmountByIdItemRepo(@PathVariable Long id_item_repo) {
        try {
            int recordNumber = itemRepoService.incrementAmountByIdItemRepo(id_item_repo);
            if (recordNumber != 1) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            ItemRepo updatedItemRepo = itemRepoService.getItemRepoByIdItemRepo(id_item_repo);
            if (updatedItemRepo == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            Item itemInfo = itemService.getItemById(updatedItemRepo.getIdItem());
            return ResponseEntity.ok(new RewardDTO(itemInfo, updatedItemRepo));
        } catch (Exception e) {
            System.out.println("Error in incrementAmountByIdItemRepo controller: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
