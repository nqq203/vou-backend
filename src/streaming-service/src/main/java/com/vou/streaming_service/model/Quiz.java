/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 14/08/2024 - 00:53
 */
package com.vou.streaming_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;



public class Quiz {
    String question;
    List<Option> options;
    int correctAnswerIndex;

//    public Quiz(String question, List<Option> options, int correctAnswerIndex) {
//        this.question = question;
//        this.options = options;
//        this.correctAnswerIndex = correctAnswerIndex;
//    }

    @JsonCreator
    public Quiz(@JsonProperty("question") String question,
                @JsonProperty("options") List<Option> options,
                @JsonProperty("correctAnswerIndex") int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Getters
    public String getQuestion() { return question; }
    public List<Option> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }

    @Override
    public String toString() {
        return "{" +
                "\"question\":\"" + question + "\"," +
                "\"options\":" + options +
                ",\"correctAnswerIndex\":" + correctAnswerIndex +
                "}";
    }

}
