package com.sprint.mission.discodeit.common.advice;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.ErrorResponse;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleException(IllegalArgumentException e) {
    log.warn("잘못된 요청입니다.", e);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleException(NoSuchElementException e) {
    log.warn("요청한 값을 찾을 수 없습니다.", e);
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.atLevel(errorCode.getLevel())
        .setCause(e)
        .log("요청 처리 실패 - code: {}, details: {}", errorCode, e.getDetails());

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
    Map<String, Object> details = new LinkedHashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(fieldError -> details.put(fieldError.getField(), fieldError.getDefaultMessage()));
    log.atLevel(errorCode.getLevel())
        .log("유효성 검사 실패 - code: {}, errorCount: {}",
            errorCode, e.getBindingResult().getErrorCount());
    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        errorCode.toString(),
        errorCode.getMessage(),
        details,
        e.getClass().getSimpleName(),
        errorCode.getStatus()
    );
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    log.error("예상하지 못한 내부 오류", e);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("예상하지 못한 서버 오류가 발생했습니다.");
  }
}
