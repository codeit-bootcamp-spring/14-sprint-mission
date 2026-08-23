package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {
    private final ExceptionType type;

    public CustomRuntimeException(ExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    // Object타입으로 받아서 Service코드에서 toString()으로 받을 필요가 없음
    public CustomRuntimeException(ExceptionType type, Object target) {
        super(String.format(type.getMessage(), target));
        this.type = type;
    }
}
