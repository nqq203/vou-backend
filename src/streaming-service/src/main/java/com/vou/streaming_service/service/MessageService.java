/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:44
 */
package com.vou.streaming_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vou.streaming_service.constants.Constants;
import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.model.UserResult;
import com.vou.streaming_service.socket.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
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

    @Autowired
    public MessageService(SocketService socketService, RedisCache redisCache, EventSchedulerService eventSchedulerService) {
        this.socketService = socketService;
        this.redisCache = redisCache;
        this.eventSchedulerService = eventSchedulerService;
        this.objectMapper = new ObjectMapper();

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

    public String saveMessage( String room, String username, String message) {
        String key = room + "_"+username;
        log.info("Key: [{}] ", key);
         this.redisCache.set(key, message);
         return message;
    }

    public void addUserToRoom(String key) {
//        String key = USER_LIST_PREFIX + room;
        redisCache.set(key,"0");
    }
    public void sendUserListUpdate(String room) {
        List<String> users = getPlayers(room);
        log.info("Users: [{}]", users.size(),room );
        socketService.sendMessage(room, String.valueOf(users.size()), "SERVER",null, Constants.AMOUNT);
    }
    public void removeUserFromRoom(String room, String username) {
        String key = room + "_"+username;
        redisCache.delete(key);
    }

    public Map<String,String> getResult(String room){
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

    public void sendMessage(String room, String message, String senderUsername, String targetUsername,String TOPIC){
        socketService.sendMessage(room,message,senderUsername,targetUsername,TOPIC);
    }

    public String startGame(){
        String room = "1";
        List<Quiz> quizz = Arrays.asList(
                new Quiz(
                        "In what continent is Indonesia?",
                        Arrays.asList(
                                new Option("0", "A", "South America"),
                                new Option("1", "B", "Europe"),
                                new Option("2", "C", "Asia")
                        ),
                        2
                ),
                new Quiz(
                        "Which continent has the highest population density?",
                        Arrays.asList(
                                new Option("0", "A", "Asia"),
                                new Option("1", "B", "South Africa"),
                                new Option("2", "C", "Australia")
                        ),
                        0
                ),
                new Quiz(
                        "What is 5x5?",
                        Arrays.asList(
                                new Option("0", "A", "20"),
                                new Option("1", "B", "25"),
                                new Option("2", "C", "10")
                        ),
                        1
                ),
                new Quiz(
                        "What is the square root of 169?",
                        Arrays.asList(
                                new Option("0", "A", "20"),
                                new Option("1", "B", "23"),
                                new Option("2", "C", "13")
                        ),
                        2
                ),
                new Quiz(
                        "What is the smallest ocean?",
                        Arrays.asList(
                                new Option("0", "A", "Atlantic Ocean"),
                                new Option("1", "B", "Pacific Ocean"),
                                new Option("2", "C", "Arctic Ocean")
                        ),
                        2
                )
        );
        saveQuizzes(quizz);
        List<Quiz> questions = getQuizzes();
        System.out.println("Question: " + questions.toString());
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        ZonedDateTime newTimePlus = zonedDateTime.plusSeconds(10);
        eventSchedulerService.scheduleJob(newTimePlus.toLocalDateTime(),()->{
            sendMessage(room, quizz.get(0).toString(), "SERVER", null,"start_game");
            ZonedDateTime zonedDateTime1 = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            ZonedDateTime newTimePlus1 = zonedDateTime1.plusSeconds(3);
            eventSchedulerService.scheduleJob(newTimePlus1.toLocalDateTime(),()->{
                sendNextQuestion(room);
            });

        });
        return newTimePlus.toString();
    }
    public void saveQuizzes(List<Quiz> quizzes) {
        try {
            String quizzesJson = objectMapper.writeValueAsString(quizzes);
            redisCache.set("quizzes", quizzesJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<Quiz> getQuizzes() {
        String quizzesJson = redisCache.get("quizzes");
        if (quizzesJson != null) {
            try {
                List<Quiz> quizzes = objectMapper.readValue(quizzesJson, new TypeReference<List<Quiz>>() {});
                return quizzes;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private void sendNextQuestion(String room) {
        List<Quiz> quizzes = getQuizzes();
        if (quizzes.isEmpty()) {
            sendMessage(room, "Game has ended", "SERVER", null, "game_end");
            return;
        }
        Quiz currentQuiz = quizzes.get(0);
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
        List<UserResult> results = calculateResults(room);
        log.info ("Result {}",results.toString());
        sendMessage(room, results.toString(), "SERVER", null, "results");
        List<Quiz> quizzes = getQuizzes();
        if (quizzes.isEmpty()) {
            sendMessage(room, "Game has ended", "SERVER", null, "game_end");
            return;
        }
        quizzes.remove(0);
        saveQuizzes(quizzes);
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

        log.info("SCORE: {}", String.valueOf(score));
        redisCache.set(key, String.valueOf(score));
    }
}
