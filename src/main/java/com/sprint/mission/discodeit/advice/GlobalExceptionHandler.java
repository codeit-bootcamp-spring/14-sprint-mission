package com.sprint.mission.discodeit.advice;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
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
        ExceptionType exceptionType = exception.getType();
        log.makeLoggingEventBuilder(exceptionType.getLogLevel())
                .setCause(exception)
                .log(exception.getMessage());
        return ResponseEntity
                .status(exceptionType.getStatus())
                .body(exceptionType.getResponse());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handle(Exception exception) {
        log.error("우리가 커버하지 못한 예외 발생", exception);
    }
}
