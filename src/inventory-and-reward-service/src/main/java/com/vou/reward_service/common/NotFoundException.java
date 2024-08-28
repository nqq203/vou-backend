package com.vou.reward_service.common;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }
}