package com.sprint.mission.discodeit.core.common.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(Instant timestamp,
                            String code,
                            String message,
                            Map<String, Object> details,
                            String exceptionType,
                            int status) {
    public static ErrorResponse of(Instant timestamp,
                                   String code,
                                   String message,
                                   Map<String, Object> details,
                                   String exceptionType,
                                   int status) {
        return new ErrorResponse(
                timestamp,
                code,
                message,
                details,
                exceptionType,
                status
        );
    }
}
