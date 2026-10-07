package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        HttpStatus status = e.getErrorCode().name().contains("NOT_FOUND")
            ? HttpStatus.NOT_FOUND
            : HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(e.getTimestamp())
            .status(status.value())
            .exceptionType(e.getClass().getSimpleName())
            .code(e.getErrorCode().name())
            .message(e.getErrorCode().getMessage())
            .details(e.getDetails())
            .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException e) {

        Map<String, Object> details = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .exceptionType(e.getClass().getSimpleName())
            .code(ErrorCode.INVALID_INPUT_VALUE.name())
            .message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
            .details(details)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .exceptionType(e.getClass().getSimpleName())
            .code("INTERNAL_SERVER_ERROR")
            .message("서버 내부에서 예상치 못한 오류가 발생했습니다.")
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
