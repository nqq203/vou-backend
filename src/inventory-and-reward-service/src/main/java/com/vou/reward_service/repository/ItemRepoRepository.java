package com.vou.reward_service.repository;

import com.vou.reward_service.model.ItemRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepoRepository extends JpaRepository<ItemRepo, Long> {
    boolean existsItemRepoByIdItemAndAmountIsGreaterThan(Long idItem, Long amount);

    ItemRepo findItemRepoByIdItem(Long idItem);
}
