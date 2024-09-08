package com.vou.streaming_service.dto;


import com.vou.streaming_service.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameInfoDTO {
    private Long gameId;
    private String name;
    private String gameType;
    private Long eventId;
    private Timestamp startedAt;
    private List<QuizDTO> quiz;


    public GameInfoDTO(Long gameId, String name, String gameType,Timestamp startedAt, Long eventId, List<QuizDTO> quiz) {
        this.gameId = gameId;
        this.name = name;
        this.gameType = gameType;
        this.startedAt = subtractSevenHours(startedAt);
        this.eventId = eventId;
        this.quiz =quiz;
    }
    public static Timestamp subtractSevenHours(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        LocalDateTime localDateTime = timestamp.toLocalDateTime().minusHours(7);
        return Timestamp.valueOf(localDateTime);
    }

}
