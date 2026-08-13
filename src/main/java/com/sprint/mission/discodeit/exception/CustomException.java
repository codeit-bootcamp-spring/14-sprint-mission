package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ExceptionType type;

    public CustomException(
            ExceptionType type,
            Object... errorData     // 가변 인자: 0-n개 처리할 수 있다
    ) {
        super(type.formatLogMessage(errorData));
        this.type = type;
    }

    public CustomException(
            ExceptionType type,
            Throwable cause,
            Object... errorData
    ) {
        super(type.formatLogMessage(errorData), cause);
        this.type = type;
    }

}
