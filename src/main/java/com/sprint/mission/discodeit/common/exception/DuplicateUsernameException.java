package com.sprint.mission.discodeit.common.exception;

import java.util.Collections;

public class DuplicateUsernameException extends UserException {

    public DuplicateUsernameException(String username) {
        super(ErrorCode.DUPLICATE_USERNAME, Collections.singletonMap("username", username));
    }
}
