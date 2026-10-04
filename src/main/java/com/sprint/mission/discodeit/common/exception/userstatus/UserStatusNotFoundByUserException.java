package com.sprint.mission.discodeit.common.exception.userstatus;

import com.sprint.mission.discodeit.common.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusNotFoundByUserException extends UserStatusException {

    public UserStatusNotFoundByUserException(UUID userId) {
        super(ErrorCode.USER_STATUS_NOT_FOUND, Map.of("userId", userId));
    }
}
