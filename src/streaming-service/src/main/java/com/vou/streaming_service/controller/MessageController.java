/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:39
 */
package com.vou.streaming_service.controller;

import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.service.EventSchedulerService;
import com.vou.streaming_service.service.MessageService;
import com.vou.streaming_service.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class MessageController{

    private final MessageService messageService;
    private RedisCache redisCache;
    private EventSchedulerService eventSchedulerService;

    @Autowired
    private QuizService quizService;

    @GetMapping("message/{room}")
    public ResponseEntity<List<String>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getPlayers(room));
    }
//
//    @PostMapping("/create")
//    public ResponseEntity<String> createGame(){
//
//        String date = messageService.startGame();
//        return ResponseEntity.ok(date);
//    }

    @PostMapping("/quiz/create")
    public ResponseEntity<List<Quiz>> createQuiz(@RequestBody List<Quiz> quizzes){
        List<Quiz> saveQuizzes = quizService.saveQuizzes(quizzes);
        return ResponseEntity.ok(saveQuizzes);
    }
}


