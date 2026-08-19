package com.sprint.mission.exception;

import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

    private final ExceptionType type;

    public DiscodeitException(
            ExceptionType type,
            Object... errorData     // 가변 인자: 0-n개 처리할 수 있다
    ) {
        super(type.formatLogMessage(errorData));
        this.type = type;
    }

    public DiscodeitException(
            ExceptionType type,
            Throwable cause,
            Object... errorData
    ) {
        super(type.formatLogMessage(errorData), cause);
        this.type = type;
    }

}
