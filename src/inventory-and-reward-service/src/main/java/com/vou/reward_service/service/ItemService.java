package com.vou.reward_service.service;

import com.vou.reward_service.common.NotFoundException;
import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.entity.CreateItemRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.repository.ItemRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // TODO: handle id_item = 0;
    public int createItem(CreateItemRequest createItemRequest) throws Exception {
        Item item = new Item();
        item.setItemName(createItemRequest.getItemName());
        item.setDescription(createItemRequest.getDescription());
        item.setIdEvent(createItemRequest.getIdEvent());
        item.setImageUrl(createItemRequest.getImageUrl());
        itemRepository.save(item);
        return HttpStatus.CREATED;
    }

    public Item getItemById(Long id) {
        return itemRepository.findItemByIdItem(id);
    }

    public Integer updateItemById(Long id, CreateItemRequest createItemRequest) {
        Item itemFound = itemRepository.findItemByIdItem(id);
        if (itemFound == null) {
            throw new NotFoundException("Không tìm thấy item với id đã cho");
        } else {


            if (createItemRequest.getImageUrl() != null) {
                itemFound.setImageUrl(createItemRequest.getImageUrl());
            }
            if (createItemRequest.getItemName() != null) {
                itemFound.setItemName(createItemRequest.getItemName());
            }
            if (createItemRequest.getIdEvent() != null) {
                itemFound.setIdEvent(createItemRequest.getIdEvent());
            }
        }
        itemRepository.save(itemFound);
        return HttpStatus.OK;
    }

    // TODO: Delete from itemrepo first
    public Integer deleteItemById(Long id) {
        Item itemFound = itemRepository.findItemByIdItem(id);
        if (itemFound == null) {
            throw new NotFoundException("Không tìm thấy item để xóa");
        }
        itemRepository.delete(itemFound);
        return HttpStatus.OK;
    }
}
