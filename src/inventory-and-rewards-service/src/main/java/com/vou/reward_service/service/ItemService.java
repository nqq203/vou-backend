package com.vou.reward_service.service;

import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.entity.CreateItemRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.repository.ItemRepository;
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
    public int createItem(CreateItemRequest createItemRequest) {
        try {
            Item item = new Item();
            item.setItemName(createItemRequest.getItemName());
            item.setDescription(createItemRequest.getDescription());
            item.setIdEvent(createItemRequest.getIdEvent());
            item.setImageUrl(createItemRequest.getImageUrl());
            itemRepository.save(item);
            return HttpStatus.CREATED;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public Item getItemById(Long id) {
        try {
            Item itemFound = itemRepository.findItemByIdItem(id);
            if (itemFound == null) {
                return null;
            }
            return itemFound;
        } catch (Exception e) {
            return null;
        }
    }

    public Integer updateItemById(Long id, CreateItemRequest createItemRequest) {
        try {
            Item itemFound = itemRepository.findItemByIdItem(id);
            if (itemFound == null) {
                return HttpStatus.NOT_FOUND;
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
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    // TODO: Delete from itemrepo first
    public Integer deleteItemById(Long id) {
        try {
            Item itemFound = itemRepository.findItemByIdItem(id);
            if (itemFound == null) {
                return HttpStatus.NOT_FOUND;
            }
            itemRepository.delete(itemFound);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
