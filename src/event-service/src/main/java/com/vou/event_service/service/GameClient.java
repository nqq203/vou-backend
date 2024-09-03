package com.vou.event_service.service;

import com.vou.event_service.model.Game;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GameClient {
    private final RestTemplate restTemplate;
    private final String game_url = "http://streaming-service:8086/api/v1/game/";
    public GameClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Optional<Game> findGameByIdGame(Long idGame) {
        try {
            ResponseEntity<Game> response = restTemplate.getForEntity(game_url + "/" + idGame, Game.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
