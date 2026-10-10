package com.sprint.mission.discodeit.common.exception;

import java.util.Map;
import java.util.UUID;

public class DuplicateUserStatusException extends UserException {

    public DuplicateUserStatusException(UUID userId) {
        super(ErrorCode.DUPLICATE_USER_STATUS, Map.of("userId", userId));
    }
}
