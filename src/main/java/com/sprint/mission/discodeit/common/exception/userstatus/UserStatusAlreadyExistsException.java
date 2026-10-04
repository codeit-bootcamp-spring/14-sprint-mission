package com.sprint.mission.discodeit.common.exception.userstatus;

import com.sprint.mission.discodeit.common.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {

    public UserStatusAlreadyExistsException(UUID userId) {
        super(ErrorCode.DUPLICATE_USER_STATUS, Map.of("userId", userId));
    }
}
