/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 07/09/2024 - 05:48
 */
package com.vou.streaming_service.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuizRedis {
    private String question;
    private String ans1;
    private String ans2;
    private String ans3;
    private Long idGame;
    private int correctAnswerIndex;
    private String radioUrl;

    public QuizRedis() {
    }

    @JsonCreator
    public QuizRedis(
            @JsonProperty("question") String question,
            @JsonProperty("ans1") String ans1,
            @JsonProperty("ans2") String ans2,
            @JsonProperty("ans3") String ans3,
            @JsonProperty("idGame") Long idGame,
            @JsonProperty("correctAnswerIndex") int correctAnswerIndex,
            @JsonProperty("radioUrl") String radioUrl
    ) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.idGame = idGame;
        this.correctAnswerIndex = correctAnswerIndex;
        this.radioUrl = radioUrl;
    }
    public String toString() {
        Map<String, String> optionsMap = new LinkedHashMap<>();
        optionsMap.put("1", ans1);
        optionsMap.put("2", ans2);
        optionsMap.put("3", ans3);

        String optionsJson = new JSONObject(optionsMap).toString();

        String correctAnswerKey = String.valueOf(correctAnswerIndex + 1);

        return "{" +
                "\"question\":\"" + question + "\"," +
                "\"options\":" + optionsJson + "," +
                "\"correctAnswerIndex\":\"" + correctAnswerKey + "\"," +
                "\"radioUrl\":\"" + radioUrl + "\"" +
                "}";
    }

    // Getters
    public String getQuestion() { return question; }
    public String getAns1() { return ans1; }
    public String getAns2() { return ans2; }
    public String getAns3() { return ans3; }
    public Long getIdGame() { return idGame; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getRadioUrl() { return radioUrl; }

    // Setters
    public void setQuestion(String question) { this.question = question; }
    public void setAns1(String ans1) { this.ans1 = ans1; }
    public void setAns2(String ans2) { this.ans2 = ans2; }
    public void setAns3(String ans3) { this.ans3 = ans3; }
    public void setIdGame(Long idGame) { this.idGame = idGame; }
}