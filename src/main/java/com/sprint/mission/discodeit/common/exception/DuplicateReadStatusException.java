package com.sprint.mission.discodeit.common.exception;

import java.util.Map;
import java.util.UUID;

public class DuplicateReadStatusException extends ReadStatusException {

    public DuplicateReadStatusException(UUID userId, UUID channelId) {
        super(ErrorCode.DUPLICATE_READ_STATUS, Map.of("userId", userId, "channelId", channelId));
    }
}
