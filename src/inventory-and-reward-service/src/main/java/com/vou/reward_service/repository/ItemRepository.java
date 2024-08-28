package com.vou.reward_service.repository;

import com.vou.reward_service.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Override
    List<Item> findAll();
    Item findItemByIdItem(Long id_item);
}
