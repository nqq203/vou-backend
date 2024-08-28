/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 14/08/2024 - 00:53
 */
package com.vou.streaming_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vou.streaming_service.dto.QuizDTO;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;


@Getter
@Entity
public class Quiz {
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String ans1;

    private String ans2;

    private String ans3;

    private Long idGame;

    private int correctAnswerIndex;

    public Quiz(QuizDTO quizDTO, Long id_game) {
        this.question = quizDTO.getQuestion();
        this.ans1= quizDTO.getAns1();
        this.ans2= quizDTO.getAns2();
        this.ans3= quizDTO.getAns3();
        this.idGame=id_game;
        this.correctAnswerIndex = quizDTO.getCorrectAnswerIndex();
    }

    public Quiz() {
    }
    //    public Quiz(String question, List<Option> options, int correctAnswerIndex) {
//        this.question = question;
//        this.options = options;
//        this.correctAnswerIndex = correctAnswerIndex;
//    }

//    @JsonCreator
//    public Quiz(@JsonProperty("question") String question,
//                @JsonProperty("options") List<Option> options,
//                @JsonProperty("correctAnswerIndex") int correctAnswerIndex) {
//        this.question = question;
//        this.options = options;
//        this.correctAnswerIndex = correctAnswerIndex;
//    }
//
//    @Override
//    public String toString() {
//        return "{" +
//                "\"question\":\"" + question + "\"," +
//                "\"options\":" + options +
//                ",\"correctAnswerIndex\":" + correctAnswerIndex +
//                "}";
//    }

}
