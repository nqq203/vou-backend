package com.vou.streaming_service.client;

import com.vou.streaming_service.dto.RewardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class EventClient {
    // For increasing share count
    private final RestTemplate restTemplate;

    @Autowired
    public EventClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private final String EVENTS_URL = "http://event-service:8083/api/v1/events";

    public Optional<Integer> increaseShareCount(Long id_event) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    EVENTS_URL + "/event-statistics/" + id_event,
                    HttpMethod.PUT,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error event client in streaming: " + e.getMessage());
            return Optional.empty();
        }
    }
}
