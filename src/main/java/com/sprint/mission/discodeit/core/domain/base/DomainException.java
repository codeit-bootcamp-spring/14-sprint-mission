package com.sprint.mission.discodeit.core.domain.base;

import lombok.Getter;
import org.slf4j.event.Level;

import java.time.Instant;
import java.util.Map;

@Getter
public abstract class DomainException extends RuntimeException{
    private final Instant timestamp;
    private final ErrorCode type;
    private final Map<String, Object> details;

    protected DomainException(ErrorCode type, Map<String, Object> details) {
        this(type, details, null);
    }

    protected DomainException(ErrorCode type, Map<String, Object> details, Exception cause) {
        super(type.getTitle() + type.getDescription(), cause);
        this.timestamp = Instant.now();
        this.type = type;
        this.details = details;
    }

    public String getExceptionClassName() {
        return getClass().getName();
    }

    public String getExceptionTypeName() {
        return type.toString();
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
