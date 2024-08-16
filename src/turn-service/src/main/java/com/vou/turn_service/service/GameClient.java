package com.vou.turn_service.service;

import com.vou.turn_service.model.PlaySession;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GameClient {
    private final RestTemplate restTemplate;
    private final String gameServiceUrl = "http://user-service:8082/api/v1/game";

    @Autowired
    public GameClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public PlaySession getPlaySessionByIdPlayerAndIdGame(Long idPlayer, Long idGame) {
        try {
            ResponseEntity<PlaySession> response = restTemplate.getForEntity(gameServiceUrl + "/" + idPlayer + "/" + idGame, PlaySession.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (RestClientException e) {
            System.err.println("Error retrieving play session in client: " + e.getMessage());
            return null;
        }
    }

    public byte checkExistPlaySession(Long idPlayer, Long idGame, Long idPlaySession) {
        ResponseEntity<Boolean> response;
        if (idPlaySession != null)
            response = restTemplate.getForEntity(gameServiceUrl + "/" + idPlaySession, Boolean.class);
        else
            response = restTemplate.getForEntity(gameServiceUrl + "/" + idPlayer + "/" + idGame, Boolean.class);
        try {
            // Exist play session
            if (response.getStatusCode() == HttpStatus.OK) {
                return 0;
            }
            // Not exist play session
            return 1;
        } catch (RestClientException e) {
            // Error check play session
            System.err.println("Error checking exist play session: " + e.getMessage());
            return 2;
        }
    }

    public PlaySession createPlaySession(PlaySession playSession) {
        try {
            ResponseEntity<PlaySession> response = restTemplate.postForEntity(gameServiceUrl + "/", playSession, PlaySession.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                System.out.println("Failed to create play session: " + response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            System.err.println("RestClientException when creating play session: " + e.getMessage());
            return null;
        }
    }

    public PlaySession updatePlaySession(PlaySession playSession) {
//        try {
//            ResponseEntity<PlaySession> response = restTemplate.put
//        }
        return null;
    }
}