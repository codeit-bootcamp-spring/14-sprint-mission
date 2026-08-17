package com.sprint.mission.discodeit.exception;

import java.time.Instant;

/**
 * 이 프로젝트가 던지는 의도적 예외는 전부 이 타입 하나로 통일.
 * 상태 코드·기본 메시지는 ExceptionType이 들고 있으므로, 새 예외 상황이 생겨도
 * enum 값 하나만 추가하면 되고 GlobalExceptionHandler는 손댈 필요가 없다.
 */
public class DiscodeitException extends RuntimeException {

    private final ExceptionType type;
    private final Instant timestamp;

    public DiscodeitException(ExceptionType type) {
        this(type, type.getMessage());
    }

    public DiscodeitException(ExceptionType type, String message) {
        super(message);
        this.type = type;
        this.timestamp = Instant.now();
    }

    public ExceptionType getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
