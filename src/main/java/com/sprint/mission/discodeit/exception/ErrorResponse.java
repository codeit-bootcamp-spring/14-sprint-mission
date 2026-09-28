package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public record ErrorResponse(
    String code,
    String message,
    int status,
    Instant timestamp
) {

    public static ErrorResponse from(DiscodeitException exception) {
        return new ErrorResponse(
            exception.getErrorCode().name(),
            exception.getMessage(),
            exception.getErrorCode().getHttpStatus().value(),
            exception.getTimeStamp()
        );
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
            errorCode.name(),
            message,
            errorCode.getHttpStatus().value(),
            Instant.now()
        );
    }
}
