package com.vou.streaming_service.service;

import com.vou.streaming_service.dto.PlayerDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class PlayerService {
    private final RestTemplate restTemplate;
    private final String PLAYER_URL = "http://inventory-and-reward-service:8082/api/v1/players";

    public PlayerService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<PlayerDTO> findPlayerByIdentifier(String email, String username, Long idUser) {
        try {
            String url;
            if (email != null) {
                url = PLAYER_URL + "?email=" + email;
            } else if (username != null) {
                url = PLAYER_URL + "?username=" + username;
            } else {
                url = PLAYER_URL + "?id_user=" + idUser;
            }
            ResponseEntity<PlayerDTO> response = restTemplate.getForEntity(url, PlayerDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
