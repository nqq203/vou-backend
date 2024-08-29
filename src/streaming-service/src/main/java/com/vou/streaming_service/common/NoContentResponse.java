package com.vou.streaming_service.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoContentResponse implements ApiResponse {
    private int status = HttpStatus.NO_CONTENT.value();
    private String message;

    public NoContentResponse() {}

    public NoContentResponse(String msg) {
        message = msg;
    }
}
