package com.vou.statistics_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameClient {
    private final RestTemplate restTemplate;
    private final String userUrl = "http://game-service:8086/api/v1/game";

    @Autowired
    public GameClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


}
