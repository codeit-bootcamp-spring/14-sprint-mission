package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DiscodeitException.class)

    public ResponseEntity<ErrorResponse> discodeitExceptionHandler(
        DiscodeitException exception
    ) {
        log.warn("[{}] {}", exception.getErrorCode(), exception.getErrorCode());
        ErrorResponse response = ErrorResponse.from(exception);
        return ResponseEntity.status(
            exception.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> allExceptionExceptionHandler(
        Exception exception
    ) {
        log.error("서버 오류", exception);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.internalServerError().body(response);
    }

}
