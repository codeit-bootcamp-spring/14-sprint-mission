package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidPasswordException extends AuthException {

    public InvalidPasswordException(String username) {
        super(ErrorCode.INVALID_INPUT_VALUE, Map.of("username", username));
    }

    public InvalidPasswordException(Map<String, Object> details) {
        super(ErrorCode.INVALID_INPUT_VALUE, details);
    }
}
