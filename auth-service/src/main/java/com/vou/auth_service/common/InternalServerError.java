package com.vou.auth_service.common;
import org.springframework.http.HttpStatus;

public class InternalServerError extends ErrorResponse {
    public InternalServerError() {
        super("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public InternalServerError(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}