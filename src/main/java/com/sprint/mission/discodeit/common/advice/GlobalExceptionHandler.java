package com.sprint.mission.discodeit.common.advice;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.ErrorResponse;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleException(IllegalArgumentException e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleException(NoSuchElementException e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorCode errorCode = e.getErrorCode();

    ErrorResponse errorResponse = new ErrorResponse(
        e.getTimestamp(),
        errorCode.toString(),
        errorCode.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        errorCode.getStatus()
    );
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ErrorCode errorCode = ErrorCode.INCORRECT_REQUEST;
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        errorCode.toString(),
        errorCode.getMessage(),
        Map.of(),
        e.getClass().getSimpleName(),
        errorCode.getStatus()
    );
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    e.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(e.getMessage());
  }
}
