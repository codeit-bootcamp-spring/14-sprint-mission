package com.sprint.mission.advice;

import com.sprint.mission.DiscodeitException;
import com.sprint.mission.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<String> handle(DiscodeitException exception) {
        ExceptionType discodeitExceptionType = exception.getType();
        log.makeLoggingEventBuilder(discodeitExceptionType.getLogLevel())
                .setCause(exception)
                .log(exception.getMessage());
        return ResponseEntity
                .status(discodeitExceptionType.getStatus())
                .body(discodeitExceptionType.getResponse());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handle(Exception exception) {
        log.error("우리가 커버하지 못한 예외 발생", exception);
    }
}
