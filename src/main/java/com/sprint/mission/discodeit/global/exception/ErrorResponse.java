package com.sprint.mission.discodeit.global.exception;

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
    static public ErrorResponse from(CustomException exception){
        CustomErrorCode errorCode = exception.getCustomErrorCode();

        return new ErrorResponse(
            exception.getTimestamp(),
            errorCode.name(),
            errorCode.getMessage(),
            exception.getDetails(),
            exception.getClass().getSimpleName(),
            errorCode.getStatus().value()
        );
    }

    static public ErrorResponse fromValidationFailure(
        Exception exception,
        Map<String, Object> details
    ) {
        CustomErrorCode errorCode = CustomErrorCode.VALIDATION_FAILED;

        return new ErrorResponse(
            Instant.now(),
            errorCode.name(),
            errorCode.getMessage(),
            details,
            exception.getClass().getSimpleName(),
            errorCode.getStatus().value()
        );
    }
}