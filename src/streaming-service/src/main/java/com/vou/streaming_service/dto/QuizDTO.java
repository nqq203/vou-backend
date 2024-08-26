package com.vou.streaming_service.dto;


import com.vou.streaming_service.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long quizId;
    private String question;
    private String ans1;
    private String ans2;
    private String ans3;
    private int correctAnswerIndex;

    public QuizDTO(Quiz quiz) {
        this.quizId= quiz.getId();
        this.question= quiz.getQuestion();
        this.ans1=quiz.getAns1();
        this.ans2=quiz.getAns2();
        this.ans3=quiz.getAns3();
        this.correctAnswerIndex=quiz.getCorrectAnswerIndex();
    }
}
