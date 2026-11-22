package com.sprint.mission.discodeit.adapter.in.web.common;

import com.sprint.mission.discodeit.core.domain.base.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handle(DomainException exception) {
        log.makeLoggingEventBuilder(exception.getLogLevel())
                .setCause(exception)
                .log("[{}] {}: {}", exception.getExceptionTypeName(), exception.getTitle(), exception.getDescription());

        ErrorResponse response = ErrorResponseMapper.toResponse(exception);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        Map<String, Object> details = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            details.put(
                    fieldError.getField(),
                    fieldError.getRejectedValue()
            );
        }

        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity
                .status(status)
                .body(
                        ErrorResponse.of(
                                Instant.now(),
                                "REQUEST_ARGUMENTS_VALIDATION_FAILED",
                                exception.getMessage(),
                                details,
                                exception.getClass().getName(),
                                status
                        )
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handle(AuthenticationException exception, HttpServletRequest request) {
        log.info("인증 실패(401) ip={} path={}",
                request.getRemoteAddr(),
                request.getRequestURI()
        );

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity
                .status(status)
                .body(
                        ErrorResponse.of(
                                Instant.now(),
                                status.name(),
                                "인증되지 않았습니다.",
                                Map.of(),
                                exception.getClass().getName(),
                                status.value()
                        )
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handle(AccessDeniedException exception, HttpServletRequest request) {
        log.warn("인가 실패(403) ip={} path={}",
                request.getRemoteAddr(),
                request.getRequestURI()
        );

        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity
                .status(status)
                .body(
                        ErrorResponse.of(
                                Instant.now(),
                                status.name(),
                                "권한이 없습니다.",
                                Map.of(),
                                exception.getClass().getName(),
                                status.value()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Throwable throwable) {
        log.makeLoggingEventBuilder(Level.ERROR)
                .setCause(throwable)
                .log("의도하지 않은 오류 발생");
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.of(
                        Instant.now(),
                        throwable.getClass().getName(),
                        throwable.getMessage(),
                        null,
                        null,
                        status
                ));
    }
}
