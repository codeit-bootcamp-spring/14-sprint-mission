package com.sprint.mission.discodeit.exception;

public class NoSuchElementException extends DiscodeitException{

    public NoSuchElementException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
