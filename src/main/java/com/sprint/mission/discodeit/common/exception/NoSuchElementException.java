package com.sprint.mission.discodeit.common.exception;

public class NoSuchElementException extends DiscodeitException{

    public NoSuchElementException() {
        super(ErrorCode.NO_SUCH_ELEMENT);
    }
}
