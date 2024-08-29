/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 28/08/2024 - 15:33
 */
package com.vou.streaming_service.service;

import com.vou.streaming_service.dto.RewardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class RewardService {
    private final RestTemplate restTemplate;
    private final String ITEM_REPOS_URL = "http://inventory-and-reward-service:8087/api/v1/item-repos";

    @Autowired
    public RewardService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<List<RewardDTO>> getRewardListByIdPlayer(Long idPlayer) {
        try {
            ResponseEntity<List<RewardDTO>> response = restTemplate.exchange(
                    ITEM_REPOS_URL + "/" + idPlayer,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RewardDTO>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error retrieving sessions: " + e.getMessage());
            return Optional.empty();
        }
    }

    public RewardDTO incrementAmountByIdItemRepo(Long idItemRepo, Long updatedAmount) {
        try {
            HttpEntity<Long> requestEntity = new HttpEntity<>(updatedAmount);
            ResponseEntity<RewardDTO> response = restTemplate.exchange(
                    ITEM_REPOS_URL + "/" + idItemRepo,
                    HttpMethod.PUT,
                    requestEntity,
                    RewardDTO.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                System.err.println("Failed to update in updateAmountInRepoItem, status code: " + response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            System.err.println("Error in updateAmountInRepoItem: "  + e.getMessage());
            return null;
        }
    }

//    public ItemRepo[] getItem(Long id_event) {
////        return new String[]{"Cá", "Vịt", "Gà"};
//        return  null;
//    }
//
//    public ItemRepo updateQuantityItem(Long id_item) throws Exception {
//        ItemRepo updateItemRepo;
//        try{
//            updateItemRepo = itemRepo.findById(id_item).orElse(null);
//
//        } catch (Exception e) {
//            throw new Exception("Error updating itemrepo", e);
//        }
//        System.out.println("User updating: " + updateItemRepo);
//        updateItemRepo.setQuantity(updateItemRepo.getQuantity() +1 );
//        itemRepo.save(updateItemRepo);
//        return updateItemRepo;
//    }


}
