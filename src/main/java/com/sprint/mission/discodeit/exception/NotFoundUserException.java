package com.sprint.mission.discodeit.exception;

public class NotFoundUserException extends DiscodeitException{

    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
