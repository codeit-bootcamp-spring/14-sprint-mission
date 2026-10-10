package com.sprint.mission.discodeit.common.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        Map<String, Object> details,
        String exceptionType,
        int status
) {

    public static ErrorResponse from(DiscodeitException e) {
        ErrorCode errorCode = e.getErrorCode();
        return new ErrorResponse(
                e.getTimestamp(),
                errorCode.name(),
                errorCode.getMessage(),
                e.getDetails(),
                e.getClass().getSimpleName(),
                errorCode.getStatus().value()
        );
    }

    public static ErrorResponse of(Exception e, HttpStatus status, String code, String message){
        return new ErrorResponse(
                Instant.now(),
                code,
                message,
                Map.of(),
                e.getClass().getSimpleName(),
                status.value()
        );
    }
}
