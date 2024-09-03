package com.vou.streaming_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {
    private final Logger logger = LoggerFactory.getLogger(NotificationConsumerService.class);

    @KafkaListener(topics = "events", groupId = "group_id")
    public void consume(String message) {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        // Process the message and send a notification
    }
}