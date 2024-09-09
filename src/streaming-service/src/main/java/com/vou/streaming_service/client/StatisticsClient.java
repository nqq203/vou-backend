package com.vou.streaming_service.client;

import com.vou.streaming_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StatisticsClient {
    private final RestTemplate restTemplate;

    @Autowired
    public StatisticsClient(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    private String STATISTICS_URL = "http://statistics-service:8015/api/v1/statistics";

    public Optional<String> saveWinner(Long id_event, List<UserResult> userResult) {
        try {
            userResult.sort((r1, r2) -> {
                int score1 = Integer.parseInt(r1.getScore());
                int score2 = Integer.parseInt(r2.getScore());
                return Integer.compare(score1, score2);
            });
            List<PlayerResult> rawRequest = IntStream.range(0, userResult.size())
                    .mapToObj(i -> new PlayerResult(id_event, userResult.get(i).getUserId(), i))
                    .collect(Collectors.toList());
            HttpEntity<PlayerResultRequest> request = new HttpEntity<>(new PlayerResultRequest(rawRequest));
            ResponseEntity<String> response = restTemplate.exchange(
                    STATISTICS_URL + "/player-result",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error statistics client in streaming: " + e.getMessage());
            return Optional.empty();
        }
    }

//    public Optional<QuizGameStats> saveQuizGameStats(Long id_event, Long id_game, Long id_player, Integer rank) {
//        try {
//            HttpEntity<SaveQuizParticipantRequest> request = new HttpEntity<>(new SaveQuizParticipantRequest(id_event, id_game));
//            ResponseEntity<QuizGameStats> response = restTemplate.exchange(
//                    STATISTICS_URL + "/quiz-participants",
//                    HttpMethod.PUT,
//                    request,
//                    new ParameterizedTypeReference<>() {}
//            );
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return Optional.ofNullable(response.getBody());
//            }
//            return Optional.empty();
//        } catch (Exception e) {
//            System.err.println("Error statistics client in streaming: " + e.getMessage());
//            return Optional.empty();
//        }
//    }

//    public Optional<QuizQuestionStats> saveQuizQuestionStats(Long id_event, Long id_game, Long id_quiz, Boolean result) {
//        try {
//            HttpEntity<SaveQuestionStatsRequest> request = new HttpEntity<>(new SaveQuestionStatsRequest(id_quiz, id_game, id_event, result));
//            ResponseEntity<QuizQuestionStats> response = restTemplate.exchange(
//                    STATISTICS_URL + "/quiz-question-stats",
//                    HttpMethod.PUT,
//                    request,
//                    new ParameterizedTypeReference<>() {}
//            );
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return Optional.ofNullable(response.getBody());
//            }
//            return Optional.empty();
//        } catch (Exception e) {
//            System.err.println("Error statistics client in streaming: " + e.getMessage());
//            return Optional.empty();
//        }
//    }
}
