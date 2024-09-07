package com.vou.streaming_service.controller;

import com.vou.streaming_service.dto.EventDTO;
import com.vou.streaming_service.dto.GameInfoDTO;
import com.vou.streaming_service.dto.Notification;
import com.vou.streaming_service.dto.QuizDTO;
import com.vou.streaming_service.model.Game;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.service.NotificationConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    NotificationConsumerService notificationConsumerService;

    @GetMapping("/notification")
    public ResponseEntity<?> Notification(@RequestParam String username){
        List<Notification> event = notificationConsumerService.getEventNotification(username);
        return ResponseEntity.ok(event);
    }
}
