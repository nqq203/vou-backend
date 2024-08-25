package com.vou.event_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoDTO {
    private String name;
    private String gameType;
    private List<QuizDTO> quiz;
    private Long eventId;
}
