package com.vou.auth_service.config;

import com.vou.auth_service.common.BadRequest;
import com.vou.auth_service.common.NotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy trang"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Yêu cầu không dụng"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Từ chối yêu cầu"));
    }
}
