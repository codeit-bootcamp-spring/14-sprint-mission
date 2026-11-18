package com.sprint.mission.discodeit.common.advice;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitRuntimeException.class)
  public ResponseEntity<ErrorResponse> handle(DiscodeitRuntimeException exception) {
    ErrorCode code = exception.getErrorCode();
    log.makeLoggingEventBuilder(code.getLevel())
        .setCause(exception)
        .log(exception.getMessage());
    ErrorResponse response = ErrorResponse.from(exception);
    return ResponseEntity.status(code.getStatus()).body(response);
  }
}
