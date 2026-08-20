package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Void> handle(CustomException exception) {
        ExceptionType type = exception.getType();
        log.makeLoggingEventBuilder(Level.WARN)
                .setCause(exception)
                .log(type.getDescription());
        return ResponseEntity
                .status(type.getHttpStatus())
                .body(null);
    }
}
