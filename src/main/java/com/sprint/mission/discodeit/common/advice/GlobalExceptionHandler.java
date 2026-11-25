package com.sprint.mission.discodeit.common.advice;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitRuntimeException.class)
  public ResponseEntity<ErrorResponse> handle(DiscodeitRuntimeException exception) {
    ErrorCode code = exception.getErrorCode();
    HttpStatus status = HttpStatusConverter.from(code);
    ErrorResponse response = ErrorResponse.from(exception, status.value());
    log.warn(exception.getMessage(), exception);
    return ResponseEntity.status(status).body(response);
  }

}
