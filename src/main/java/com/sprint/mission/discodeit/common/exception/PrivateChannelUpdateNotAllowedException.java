package com.sprint.mission.discodeit.common.exception;

import java.util.Map;
import java.util.UUID;

public class PrivateChannelUpdateNotAllowedException extends ChannelException {

    public PrivateChannelUpdateNotAllowedException(UUID channelId) {
        super(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT, Map.of("channelId", channelId));
    }
}
