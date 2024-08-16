/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:39
 */
package com.vou.streaming_service.controller;

import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.Option;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.service.EventSchedulerService;
import com.vou.streaming_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController{

    private final MessageService messageService;
    private RedisCache redisCache;
    private EventSchedulerService eventSchedulerService;

    @GetMapping("/{room}")
    public ResponseEntity<List<String>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getPlayers(room));
    }

    @PostMapping("/game/create")
    public ResponseEntity<String> createGame(){

        String date = messageService.startGame();
        return ResponseEntity.ok(date);
    }




}


