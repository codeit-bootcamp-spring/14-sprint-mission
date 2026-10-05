package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e) {
        Map<String, Object> details = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
            details.merge(fieldError.getField(), fieldError.getDefaultMessage(),
                (existing, added) -> existing + ", " + added)
        );

        log.warn("요청 유효성 검증 실패: {}", details);

        ErrorResponse body = new ErrorResponse(
            Instant.now(),
            "VALIDATION_FAILED",
            "요청 값이 올바르지 않습니다.",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        HttpStatus status = resolveStatus(e.getErrorCode());
        log.warn("비즈니스 예외 발생: code={}, details={}", e.getErrorCode(), e.getDetails());

        ErrorResponse body = new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().name(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            status.value()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 예외 발생", e);

        ErrorResponse body = new ErrorResponse(
            Instant.now(),
            "INTERNAL_SERVER_ERROR",
            "서버 내부 오류가 발생했습니다.",
            Map.of(),
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private HttpStatus resolveStatus(ErrorCode code) {
        return switch (code) {
            case USER_NOT_FOUND,
                 USER_STATUS_NOT_FOUND,
                 CHANNEL_NOT_FOUND,
                 MESSAGE_NOT_FOUND,
                 BINARY_CONTENT_NOT_FOUND,
                 READ_STATUS_NOT_FOUND -> HttpStatus.NOT_FOUND;       // 404
            case DUPLICATE_USER,
                 DUPLICATE_USER_STATUS,
                 DUPLICATE_READ_STATUS -> HttpStatus.CONFLICT;        // 409
            case PRIVATE_CHANNEL_UPDATE -> HttpStatus.FORBIDDEN;      // 403
            case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;      // 401
        };
    }


}
