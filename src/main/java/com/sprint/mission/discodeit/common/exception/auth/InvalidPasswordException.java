package com.sprint.mission.discodeit.common.exception.auth;

import com.sprint.mission.discodeit.common.ErrorCode;
import java.util.Map;

public class InvalidPasswordException extends AuthException {

    public InvalidPasswordException(String username) {
        super(ErrorCode.INVALID_PASSWORD, Map.of("username", username));
    }
}
