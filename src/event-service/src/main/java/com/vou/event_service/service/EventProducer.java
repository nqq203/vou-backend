package com.vou.event_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vou.event_service.dto.MessageData;
import com.vou.event_service.model.Event;
import com.vou.event_service.model.FavouriteEvent;
import com.vou.event_service.repository.EventRepository;
import com.vou.event_service.repository.FavouriteEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    private static final String TOPIC = "events";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private FavouriteEventRepository favouriteEventRepository;

    @Scheduled(fixedRate = 60000)
    public void checkUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();

        // Tính toán thời gian cách hiện tại 3 ngày
        LocalDateTime threeDaysBefore = now.plusDays(3);

        // Chuyển đổi LocalDateTime sang Timestamp
        Timestamp currentDate = Timestamp.valueOf(now);
        Timestamp startDateCheck = Timestamp.valueOf(threeDaysBefore);

        List<Event> upcomingEvents = eventRepository.findEventsStartingInThreeDays(currentDate, startDateCheck);


        // Gửi thông báo cho mỗi sự kiện
        for (Event event : upcomingEvents) {
            List<FavouriteEvent> favouriteEvents = favouriteEventRepository.findAllByIdEvent(event.getIdEvent());
            List<String> usernames = favouriteEvents.stream()
                    .map(FavouriteEvent::getUsername) // Lấy idUser từ mỗi FavouriteEvent
                    .collect(Collectors.toList());
            long days = (event.getStartDate().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
            for (String username: usernames) {
                MessageData messageData = new MessageData(
                        event.getIdEvent(),
                        event.getImageUrl(),
                        "Sự kiện " + event.getEventName() + " sẽ sớm được bắt đầu!",
                        (int)days
                );
                String messageJson = username+ "--"+convertToJsonString(messageData);

                sendMessage(messageJson);
            }
        }
    }
    private String convertToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    public void sendMessage(String message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(TOPIC, message);
    }
}