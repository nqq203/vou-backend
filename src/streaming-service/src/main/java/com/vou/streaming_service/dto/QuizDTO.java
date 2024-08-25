package com.vou.streaming_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class QuizDTO {
    private String question;
    private String ans1;
    private String ans2;
    private String ans3;
    private int correctAnswerIndex;

    public QuizDTO(String question, String ans1, String ans2, String ans3, int correctAnswerIndex) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
