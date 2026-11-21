package com.sprint.mission.discodeit.global.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final Instant timestamp;
    private final CustomErrorCode customErrorCode;
    private final Map<String, Object> details;

    public CustomException(CustomErrorCode errorCode) {
        this(errorCode, Map.of());      // 기존 코드 안망가지게
    }

    public CustomException(CustomErrorCode customErrorCode, Map<String, Object> details) {
        super(customErrorCode.getMessage());
        this.timestamp = Instant.now();
        this.customErrorCode = customErrorCode;
        this.details = Map.copyOf(details);
    }
}
