package com.vou.event_service.common;
import org.springframework.http.HttpStatus;

public class CreatedResponse extends SuccessResponse {
    public CreatedResponse(Object metadata) {
        super("Created", HttpStatus.CREATED, metadata);
    }

    public CreatedResponse(String message, Object metadata) {
        super(message, HttpStatus.CREATED, metadata);
    }
}