package com.vou.event_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private String question;
    private String a;
    private String b;
    private String c;
    private int correctAnswerIndex;
}
