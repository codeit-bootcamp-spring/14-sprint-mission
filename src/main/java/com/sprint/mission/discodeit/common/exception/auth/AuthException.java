package com.sprint.mission.discodeit.common.exception.auth;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import java.util.Map;

public class AuthException extends DiscodeitException {

    public AuthException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
