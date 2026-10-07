package com.sprint.mission.discodeit.common.exception.user;

import com.sprint.mission.discodeit.common.ErrorCode;
import java.util.Map;

public class UsernameAlreadyExistsException extends UserException {

    public UsernameAlreadyExistsException(String username) {
        super(ErrorCode.DUPLICATE_USER_NAME, Map.of("username", username)
        );
    }
}
