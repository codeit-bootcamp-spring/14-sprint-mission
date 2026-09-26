package com.sprint.mission.discodeit.common.exception;

public class NotFoundChannelException extends DiscodeitException{

    public NotFoundChannelException() {
        super(ErrorCode.NOT_FOUND_CHANNEL);
    }
}
