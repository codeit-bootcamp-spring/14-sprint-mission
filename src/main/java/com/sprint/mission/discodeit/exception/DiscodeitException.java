package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DiscodeitException extends RuntimeException {

    Instant timestamp;
    ErrorCode errorCode;
    Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }

    public DiscodeitException(ErrorCode errorCode) {
        this(errorCode, null);
    }
}
