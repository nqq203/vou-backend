package com.vou.statistics_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventClient {

    private final RestTemplate restTemplate;
    private final String userUrl = "http://event-service:8083/api/v1/events";

    @Autowired
    public EventClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
