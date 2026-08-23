package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ExceptionType type;

    public CustomException(ExceptionType type) {
        super(type.getTitle() + type.getDescription());
        this.type = type;
    }
}
