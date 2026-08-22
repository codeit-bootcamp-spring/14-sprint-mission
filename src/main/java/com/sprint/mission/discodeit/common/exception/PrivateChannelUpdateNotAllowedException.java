package com.sprint.mission.discodeit.common.exception;

public class PrivateChannelUpdateNotAllowedException extends DiscodeitException{

    public PrivateChannelUpdateNotAllowedException() {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT);
    }
}
