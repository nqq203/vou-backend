/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 15/08/2024 - 13:47
 */
package com.vou.streaming_service.model;

public class UserResult {
    private String userId;
    private String score;

    // Constructor
    public UserResult(String userId, String score) {
        this.userId = userId;
        this.score = score;
    }

    @Override
    public String toString() {
        return "{" +
                "userId='" + userId + '\'' +
                ", score=" + score +
                '}';
    }
}