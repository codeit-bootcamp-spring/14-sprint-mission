package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public abstract class DiscodeitException extends RuntimeException {

    private final Instant timestamp;
    private final ErrorCode errorCode;

    private final Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode) {
        this(errorCode, Map.of());
    }

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.details = details;
    }


}

