package com.sprint.mission.discodeit.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ErrorCodeStatusMapper errorCodeStatusMapper;

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handle(DiscodeitException e) {
    HttpStatus status = errorCodeStatusMapper.toStatus(e.getErrorCode());
    ErrorResponse body = new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().name(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            status.value()
    );
    if (status.is4xxClientError()) {
      log.warn("4xxError : {}", body);
    }
    if (status.is5xxServerError()) {
      log.error("5xxError : {}", body, e);
    }
    return ResponseEntity.status(status).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {

    Instant now = Instant.now();
    List<FieldError> errors = e.getBindingResult().getFieldErrors();
      Map<String, Object> detail = errors.stream()
              .collect(Collectors.toMap(
                      FieldError::getField, FieldError::getDefaultMessage,
                      (oldMessage, newMessage) -> oldMessage + ", " + newMessage
              ));
    HttpStatus status = errorCodeStatusMapper.toStatus(ErrorCode.INVALID_INPUT_VALUE);
    ErrorResponse body = new ErrorResponse(
            now,
            ErrorCode.INVALID_INPUT_VALUE.name(),
            ErrorCode.INVALID_INPUT_VALUE.getMessage(),
            detail,
            e.getClass().getSimpleName(),
            status.value()
    );
    log.warn("ValidFail : {}", body);
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(NoResourceFoundException e) {

    Instant now = Instant.now();
    HttpStatus status = errorCodeStatusMapper.toStatus(ErrorCode.RESOURCE_NOT_FOUND);
    ErrorResponse body = new ErrorResponse(
            now,
            ErrorCode.RESOURCE_NOT_FOUND.name(),
            ErrorCode.RESOURCE_NOT_FOUND.getMessage(),
            Map.of(),
            e.getClass().getSimpleName(),
            status.value()
    );
    log.warn("ResourceNotFound : {}", body);
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(body);

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {

    Instant now = Instant.now();
    HttpStatus status = errorCodeStatusMapper.toStatus(ErrorCode.INTERNAL_SERVER_ERROR);
    ErrorResponse body = new ErrorResponse(
            now,
            ErrorCode.INTERNAL_SERVER_ERROR.name(),
            ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
            Map.of(),
            e.getClass().getSimpleName(),
            status.value()
    );
    log.error("InternalError : {}", body, e);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(body);
  }
}
