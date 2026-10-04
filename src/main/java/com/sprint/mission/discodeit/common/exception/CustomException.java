package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException{
    private final IExceptionType type;

    protected CustomException(IExceptionType type) {
        super(type.getTitle() + type.getDescription());
        this.type = type;
    }

    public HttpStatus getHttpStatus() {
        return type.getHttpStatus();
    }

    public Level getLogLevel() {
        return type.getLogLevel();
    }

    public String getTitle() {
        return type.getTitle();
    }

    public String getDescription() {
        return type.getDescription();
    }
}
