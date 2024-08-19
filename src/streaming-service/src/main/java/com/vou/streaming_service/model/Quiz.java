/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 14/08/2024 - 00:53
 */
package com.vou.streaming_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String a;

    private String b;

    private String c;

    int correctAnswerIndex;

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
