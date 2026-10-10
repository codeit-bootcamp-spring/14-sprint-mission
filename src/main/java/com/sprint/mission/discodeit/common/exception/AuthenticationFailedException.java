package com.sprint.mission.discodeit.common.exception;

import java.util.Collections;

public class AuthenticationFailedException extends UserException {

    public AuthenticationFailedException(String username) {
        super(ErrorCode.AUTHENTICATION_FAILED, Collections.singletonMap("username", username));
    }
}
