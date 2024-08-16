/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 15/08/2024 - 13:47
 */
package com.vou.streaming_service.model;

public class UserResult {
    private String userId;
    private int score;
    private String answer;
    private boolean correct;
    private long responseTime;

    // Constructor
    public UserResult(String userId, int score, String answer, boolean correct, long responseTime) {
        this.userId = userId;
        this.score = score;
        this.answer = answer;
        this.correct = correct;
        this.responseTime = responseTime;
    }

    // Getters and setters
    // ...

    @Override
    public String toString() {
        return "UserResult{" +
                "userId='" + userId + '\'' +
                ", score=" + score +
                ", answer='" + answer + '\'' +
                ", correct=" + correct +
                ", responseTime=" + responseTime +
                '}';
    }
}