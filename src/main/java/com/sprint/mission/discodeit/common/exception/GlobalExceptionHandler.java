package com.sprint.mission.discodeit.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handle(CustomException exception) {
        ExceptionType type = exception.getType();
        log.makeLoggingEventBuilder(type.getLogLevel())
                .setCause(exception)
                .log(type.getDescription());
        return ResponseEntity
                .status(type.getHttpStatus())
                .body(ErrorResponse.of(type.getResponse()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handle(Exception exception) {
        log.makeLoggingEventBuilder(Level.ERROR)
                .setCause(exception)
                .log("의도하지 않은 오류 발생");
        return ErrorResponse.of("오류가 발생했습니다. 잠시 후 다시 시도해주세요");
    }
}
