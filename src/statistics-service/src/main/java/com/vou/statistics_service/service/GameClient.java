package com.vou.statistics_service.service;

import com.vou.statistics_service.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GameClient {
    private final RestTemplate restTemplate;
    private final String GAME_URL = "http://streaming-service:8086/api/v1/game";

    @Autowired
    public GameClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Game> getGameByIdEvent(Long idEvent) {
        try {
            ResponseEntity<Game> response = restTemplate.exchange(
                    GAME_URL + "/events/" + idEvent,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error game client in statistics: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Integer> getParticipantsCount(Long idEvent) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    GAME_URL + "/" + idEvent + "/participants",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error game client in statistics: " + e.getMessage());
            return Optional.empty();
        }
    }
}
