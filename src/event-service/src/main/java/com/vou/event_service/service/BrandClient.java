package com.vou.event_service.service;

import com.vou.event_service.dto.BrandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BrandClient {
    private final RestTemplate restTemplate;
    private final String userUrl = "http://user-service:8082/api/v1/users";
    private final String brandUrl = "http://user-service:8082/api/v1/brands";

    @Autowired
    public BrandClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<String> getBrandLogo(Long idBrand) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(userUrl + "/" + idBrand + "/events", String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Error in getBrandLogo: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<BrandDTO> getBrandByIdBrand(Long idBrand) {
        try {
            ResponseEntity<BrandDTO> response = restTemplate.getForEntity(brandUrl + "/" + idBrand, BrandDTO.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Error in getBrandByIdBrand" + e.getMessage());
            return Optional.empty();
        }
    }
}
