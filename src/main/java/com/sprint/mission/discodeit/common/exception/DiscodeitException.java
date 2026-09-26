package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class DiscodeitException extends RuntimeException{

    private final ErrorCode errorCode;

    public DiscodeitException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

