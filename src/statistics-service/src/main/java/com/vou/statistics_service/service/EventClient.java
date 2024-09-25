package com.vou.statistics_service.service;

import com.vou.statistics_service.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class EventClient {

    private final RestTemplate restTemplate;
    private final String EVENTS_URL = "http://event-service:8083/api/v1/events";

    @Autowired
    public EventClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Event> getEvent(Long eventId) {
        try {
            ResponseEntity<Event> response = restTemplate.exchange(
                    EVENTS_URL + "/event-statistics/" + eventId,
                    HttpMethod.GET,
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
