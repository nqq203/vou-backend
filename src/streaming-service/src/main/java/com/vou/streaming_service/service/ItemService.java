/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 28/08/2024 - 15:33
 */
package com.vou.streaming_service.service;

import com.vou.streaming_service.model.ItemRepo;
import com.vou.streaming_service.repository.ItemRepoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ItemService {
    private final ItemRepoRepository itemRepo;
    private final RestTemplate restTemplate;
    private final String itemsUrl = "http://localhost:8086/api/v1/inventory-and-rewards/items";


    public ItemService(ItemRepoRepository itemRepo, RestTemplate restTemplate) {
        this.itemRepo = itemRepo;
        this.restTemplate = restTemplate;
    }


    public ItemRepo[] getItem(Long id_event) {
//        return new String[]{"Cá", "Vịt", "Gà"};
        return  null;
    }

    public ItemRepo updateQuantityItem(Long id_item) throws Exception {
        ItemRepo updateItemRepo;
        try{
            updateItemRepo = itemRepo.findById(id_item).orElse(null);

        } catch (Exception e) {
            throw new Exception("Error updating itemrepo", e);
        }
        System.out.println("User updating: " + updateItemRepo);
        updateItemRepo.setQuantity(updateItemRepo.getQuantity() +1 );
        itemRepo.save(updateItemRepo);
        return updateItemRepo;
    }
}
