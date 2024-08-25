package com.vou.event_service.service;

import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.QuizDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    @Autowired
    private RestTemplate restTemplate;

    private final String QUIZ_SERVICE_URL = "http://streaming-service:8086/api/v1/game/quiz/create";

    public void createQuiz(GameInfoDTO gameInfoDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Wrap the List<QuizDTO> in an HttpEntity
        HttpEntity<GameInfoDTO> request = new HttpEntity<>(gameInfoDTO, headers);

        // Make the POST request
        ResponseEntity<Void> response = restTemplate.exchange(
                QUIZ_SERVICE_URL,
                HttpMethod.POST,
                request,
                Void.class
        );

        // Optionally handle the response
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Quizzes created successfully.");
        } else {
            System.out.println("Failed to create quizzes: " + response.getStatusCode());
        }
    }
}