package com.sprint.mission.discodeit.exception;

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

    public static ErrorResponse from(DiscodeitException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return new ErrorResponse(
            exception.getTimestamp(),
            errorCode.name(),
            exception.getMessage(),
            exception.getDetails(),
            exception.getClass().getSimpleName(),
            errorCode.getHttpStatus().value()
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details,
        Exception exception) {
        return new ErrorResponse(
            Instant.now(),
            errorCode.name(),
            errorCode.getMessage(),
            details,
            exception.getClass().getSimpleName(),
            errorCode.getHttpStatus().value()
        );
    }
}
