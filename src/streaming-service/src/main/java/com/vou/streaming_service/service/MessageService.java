
/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:44
 */
package com.vou.streaming_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vou.streaming_service.client.StatisticsClient;
import com.vou.streaming_service.constants.Constants;
import com.vou.streaming_service.dto.QuizDTO;
import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.Option;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.model.QuizRedis;
import com.vou.streaming_service.model.UserResult;
import com.vou.streaming_service.socket.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


// Redis:
// key: Room_username
// value: score
//
@Service
@Slf4j
public class MessageService {
    private final SocketService socketService;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;
    private final EventSchedulerService eventSchedulerService;
    private final StatisticsClient statisticsClient;

    @Autowired
    public MessageService(SocketService socketService, RedisCache redisCache, EventSchedulerService eventSchedulerService, StatisticsClient statisticsClient) {
        this.socketService = socketService;
        this.redisCache = redisCache;
        this.eventSchedulerService = eventSchedulerService;
        this.objectMapper = new ObjectMapper();
        this.statisticsClient = statisticsClient;
    }

    public List<String> getPlayers(String room) {
        String pattern = room + "_*";
        Map<String, String> list = this.redisCache.getAll(pattern);


        List<String> players = list.keySet().stream()
                .filter(key -> key.startsWith(room + "_"))
                .map(key -> key.split("_")[1])
                .filter(key -> !"SERVER".equals(key))
                .collect(Collectors.toList());

        log.info("Players in room {}: {}", room, players);
        return players;
    }

    public String saveMessage(String room, String username, String message) {
        String key = room + "_" + username;
        log.info("Key: [{}] ", key);
        this.redisCache.set(key, message);
        return message;
    }

    public void sendUserListUpdate(String room) {
        List<String> users = getPlayers(room);
        log.info("Users: [{}]", users.size(), room);
        socketService.sendMessage(room, String.valueOf(users.size()), "SERVER", null, Constants.AMOUNT);
    }

    public void removeUserFromRoom(String room, String username) {
        String key = room + "_" + username;
        redisCache.delete(key);
    }

    public Map<String, String> getResult(String room) {
        String pattern = room + "_*";
        Map<String, String> list = this.redisCache.getAll(pattern);

        Map<String, String> players = list.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(room + "_"))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().split("_")[1], // Extract the username from the key
                        Map.Entry::getValue // Keep the value as is
                ));

        log.info("Players in room {}: {}", room, players);
        return players;

    }

    public void sendMessage(String room, String message, String senderUsername, String targetUsername, String TOPIC) {
        System.out.println("Send: "+message);
        socketService.sendMessage(room, message, senderUsername, targetUsername, TOPIC);
    }

    public String startGame(List<QuizDTO> quizDTOList , Long id_game, Long id_event, Timestamp startedAt){
        List<Quiz> quizz = quizDTOList.stream()
                .map(quizDTO -> new Quiz(quizDTO, id_game))
                .collect(Collectors.toList());

        saveQuizzes(quizz);
        Instant instant = startedAt.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));

        System.out.println("Scheduling task to run at: " + zonedDateTime);
        eventSchedulerService.scheduleJob(zonedDateTime.toLocalDateTime(),()->{
            System.out.println("START GAME QUIZZZZZZ");
            sendMessage(String.valueOf(id_event), "START GAME QUIZ", "SERVER", null,"start_game");
            ZonedDateTime zonedDateTime1 = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            ZonedDateTime newTimePlus = zonedDateTime1.plusSeconds(3);
            eventSchedulerService.scheduleJob(newTimePlus.toLocalDateTime(),()->{
                sendNextQuestion(String.valueOf(id_event));
            });
        });
        return startedAt.toString();
    }
    public void saveQuizzeRedis(List<QuizRedis> quizzes) {
        try {
            log.info("Quiz with saveQuizzeRedis",quizzes.toString());
            String quizzesJson = objectMapper.writeValueAsString(quizzes);
            log.info("Quzzzzz: {}", quizzesJson );

            redisCache.set("quizzes", quizzesJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void saveQuizzes(List<Quiz> quizzes) {
        try {
            List<QuizRedis> quizRedis = new ArrayList<>();
            for (Quiz item : quizzes) {
                TextToSpeechService text = new TextToSpeechService();

                String radioUrl =text.convertTextToSpeech(item.getQuestion(), item.getAns1(), item.getAns2(), item.getAns3());
                log.info("Radio convert: ", radioUrl);
                QuizRedis newItem = new QuizRedis(
                        item.getQuestion(),
                        item.getAns1(),
                        item.getAns2(),
                        item.getAns3(),
                        item.getIdGame(),
                        item.getCorrectAnswerIndex(),
                        radioUrl
                );
                quizRedis.add(newItem);
            }
            log.info("Quiz with radios: {}", quizRedis);
            String quizzesJson = objectMapper.writeValueAsString(quizRedis);
            log.info("Quzzzzz: {}", quizzesJson );

            redisCache.set("quizzes", quizzesJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<QuizRedis> getQuizzes() {
        String quizzesJson = redisCache.get("quizzes");
        if (quizzesJson != null) {
            try {
                List<QuizRedis> quizzes = objectMapper.readValue(quizzesJson, new TypeReference<List<QuizRedis>>() {});
                return quizzes;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private void sendNextQuestion(String room) {
        List<QuizRedis> quizzes = getQuizzes();
        if (quizzes.isEmpty()) {
            return;
        }
        QuizRedis currentQuiz = quizzes.get(0);
        System.out.println("Question: " + currentQuiz.toString());
        sendMessage(room, currentQuiz.toString(), "SERVER", null, "question");

        // Schedule the next question after 15 seconds
        ZonedDateTime nextQuestionTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusSeconds(15);
        eventSchedulerService.scheduleJob(nextQuestionTime.toLocalDateTime(),  () -> {
            log.info("Result");
            handleQuestionTimeout(room);
        });
    }
    private void handleQuestionTimeout(String room) {
        List<QuizRedis> quizzes = getQuizzes();
        List<UserResult> results = calculateResults(room);

        quizzes.remove(0);
        if (quizzes.isEmpty()) {
            Long idEvent = Long.valueOf(room.split("_")[0]);
            statisticsClient.saveWinner(idEvent, results);
            sendMessage(room, results.toString(), "SERVER", null, "results");
            return;
        }

        for (int i = 0; i < results.size(); i++) {
            UserResult userResult = results.get(i);
            String userId = userResult.getUserId();
            String score = userResult.getScore();
            int rank = i + 1;

            String message = String.format("Điểm của bạn là %s. Bạn đang ở hạng thứ %d/%d.",
                    score, rank, results.size());
            log.info("User: {}, message: {}", userId, message);

            // GỬI CHO userId nha
            sendMessage(room, message, "SERVER", null, "score");
        }
        log.info ("Result {}",results.toString());




        saveQuizzeRedis(quizzes);
        // Schedule sending question after 5 seconds
        ZonedDateTime sendResultsTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusSeconds(5);
        eventSchedulerService.scheduleJob(sendResultsTime.toLocalDateTime(), () -> {
            sendNextQuestion(room);
        });
    }
    private List<UserResult> calculateResults(String room) {
        List<UserResult> results = new ArrayList<>();
        List<String> userIds = getPlayers(room);

        for (String userId : userIds) {
            String score = redisCache.get(room + "_" + userId);
            log.info("{}: {}", userId, score);
            UserResult userResult = new UserResult(userId, score);
            results.add(userResult);
        }
        results.sort((r1, r2) -> {
            int score1 = Integer.parseInt(r1.getScore());
            int score2 = Integer.parseInt(r2.getScore());
            return Integer.compare(score2, score1);
        });
        return results;
    }

    public void calculateScore(JSONObject jsonObject){
        String content = jsonObject.getString("content");
        JSONObject contentJson = new JSONObject(content);
        System.out.println("Data answer: " + contentJson);

        // Extract the value of "isCorrect"
        boolean isCorrect = contentJson.getBoolean("isCorrect");
        System.out.println("Is Correct: " + isCorrect);
        Integer timer = contentJson.getInt("timer");
        Integer score =timer;
        if (isCorrect){
            score += 100;
        }
        String room = jsonObject.getString("room");
        String username = jsonObject.getString("username");
        String key = room + '_' + username;

        String value = redisCache.get(key);
        score += Integer.valueOf(value);

        log.info("SCORE: {}", String.valueOf(score));
        redisCache.set(key, String.valueOf(score));
    }
}
