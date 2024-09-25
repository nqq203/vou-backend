package com.vou.streaming_service.controller;

import com.vou.streaming_service.common.SuccessResponse;
import com.vou.streaming_service.dto.EventDTO;
import com.vou.streaming_service.dto.GameInfoDTO;
import com.vou.streaming_service.dto.Notification;
import com.vou.streaming_service.dto.QuizDTO;
import com.vou.streaming_service.model.Game;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.service.NotificationConsumerService;
import com.vou.streaming_service.socket.SocketModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    NotificationConsumerService notificationConsumerService;
    private final Logger logger = LoggerFactory.getLogger(NotificationConsumerService.class);
    @Autowired
    SocketModule socketModule;
    @GetMapping("/notification")
    public ResponseEntity<?> getNotificationByUserName(@RequestParam String username){
        List<Notification> event = notificationConsumerService.getEventNotification(username);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Đã gửi thông báo sự kiện sắp kết thúc!", HttpStatus.OK, event));
    }

    @PostMapping("/ask-for-turn")
    public ResponseEntity<?> askForTurn(@RequestParam String sender, @RequestParam String receiver){
        String message= sender +" muốn xin 1 lượt của bạn";
        Map<String, Object> response = new HashMap<>();
        response.put("sender", sender);
        response.put("receiver", receiver);
        response.put("message",message);
        socketModule.sendNotification(message, sender, receiver, "turn_notification");
        logger.info("sender"+ message+"cua"+ receiver);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Đã gửi thông báo xin lượt!", HttpStatus.OK, response));
    }
}
