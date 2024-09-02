package com.vou.auth_service.service;

import com.vou.auth_service.model.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryClient {
    private final RestTemplate restTemplate;
    private final String INVENTORY_URL = "http://inventory-and-reward-service:8087/api/v1/item-repos";

    @Autowired
    public InventoryClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<List<ItemRepo>> createItemRepo(Long idPlayer) {
        try {
            System.out.println("Create item repo with user id: " + idPlayer);
            ResponseEntity<List<ItemRepo>> response = restTemplate.exchange(
                    INVENTORY_URL + "/" + idPlayer,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<List<ItemRepo>>() {
                    }
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
}
