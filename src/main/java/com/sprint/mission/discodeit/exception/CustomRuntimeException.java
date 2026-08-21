package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException{
    private final ExceptionType type;

    public CustomRuntimeException(ExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }
}
