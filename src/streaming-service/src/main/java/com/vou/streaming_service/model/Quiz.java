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
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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

    @JsonCreator
    public Quiz(
            @JsonProperty("question") String question,
            @JsonProperty("ans1") String ans1,
            @JsonProperty("ans2") String ans2,
            @JsonProperty("ans3") String ans3,
            @JsonProperty("id_game") Long id_game,
            @JsonProperty("correctAnswerIndex") int correctAnswerIndex
    ) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.idGame = id_game;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public String toString() {
        // Create a map to represent the options with numeric keys
        Map<String, String> optionsMap = new LinkedHashMap<>();
        optionsMap.put("1", ans1);
        optionsMap.put("2", ans2);
        optionsMap.put("3", ans3);

        String optionsJson = new JSONObject(optionsMap).toString();

        String correctAnswerKey = String.valueOf(correctAnswerIndex + 1);

        return "{" +
                "\"question\":\"" + question + "\"," +
                "\"options\":" + optionsJson + "," +
                "\"correctAnswerIndex\":\"" + correctAnswerKey + "\"" +
                "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public Long getIdGame() {
        return idGame;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
