package com.vou.reward_service.service;

import com.vou.reward_service.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    private final String PLAYERS_SERVICE_URL = "http://localhost:8082/api/v1/players";

    public LinkedHashMap<String, Object> findPlayerById(Long idPlayer) {
        String url = PLAYERS_SERVICE_URL + "/" + idPlayer;

        return (LinkedHashMap<String, Object>) restTemplate.getForEntity(url, Object.class).getBody();
    }
}
