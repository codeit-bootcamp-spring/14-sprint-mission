package com.sprint.mission.discodeit.common.exception;

public class NotFoundUserException extends DiscodeitException{

    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
