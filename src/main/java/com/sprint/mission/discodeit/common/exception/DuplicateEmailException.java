package com.sprint.mission.discodeit.common.exception;

import java.util.Collections;

public class DuplicateEmailException extends UserException {

    public DuplicateEmailException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, Collections.singletonMap("email", email));
    }
}
