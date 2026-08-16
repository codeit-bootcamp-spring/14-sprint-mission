package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handle(DiscodeitException exception) {
        ExceptionType type = exception.getType();

        log.atLevel(type.getLevel())
            .setCause(exception)
            .log("{} | details = {}", type.getMessage(), exception.getDetails());

        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type));
    }
}