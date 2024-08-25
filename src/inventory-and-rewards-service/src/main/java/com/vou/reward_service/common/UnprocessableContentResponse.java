package com.vou.reward_service.common;

import org.springframework.http.HttpStatus;

public class UnprocessableContentResponse extends ErrorResponse {
    public UnprocessableContentResponse(Object errors) {
        super("Unprocessable Entity", HttpStatus.UNPROCESSABLE_ENTITY, errors);
    }

    public UnprocessableContentResponse(String message, Object errors) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, errors);
    }
}