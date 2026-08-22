package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class DiscodeitRuntimeException extends RuntimeException {
    private final ExceptionType type;

    public DiscodeitRuntimeException(ExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }
}
