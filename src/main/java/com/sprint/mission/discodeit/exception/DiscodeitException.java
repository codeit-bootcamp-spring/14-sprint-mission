package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public abstract class DiscodeitException extends RuntimeException{

    private final ErrorCode errorCode;

    public DiscodeitException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

