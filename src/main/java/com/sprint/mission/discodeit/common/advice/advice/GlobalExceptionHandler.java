package com.sprint.mission.discodeit.common.advice.advice;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitRuntimeException.class)

  public ResponseEntity<Void> handle(DiscodeitRuntimeException exception) {
    ExceptionType type = exception.getType();
    log.makeLoggingEventBuilder(type.getLevel())
        .setCause(exception)
        .log(exception.getMessage());
    return ResponseEntity.status(type.getStatus()).build();
  }
}
