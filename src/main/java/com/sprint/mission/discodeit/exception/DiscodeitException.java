package com.sprint.mission.discodeit.exception;

import java.time.Instant;

public class DiscodeitException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Instant timeStamp;

    public DiscodeitException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.timeStamp = Instant.now();
    }

    public DiscodeitException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.timeStamp = Instant.now();
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public Instant getTimeStamp() {
        return this.timeStamp;
    }

}
