package com.vou.event_service.common;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){ super(message); }
}
