package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class ChannelException extends CustomException {
    public ChannelException(ChannelExceptionType type) {
        super(type);
    }
}
