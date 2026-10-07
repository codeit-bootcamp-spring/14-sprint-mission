package com.sprint.mission.discodeit.common.exception.user;

import com.sprint.mission.discodeit.common.ErrorCode;
import java.util.Map;

public class EmailAlreadyExistsException extends UserException {

    public EmailAlreadyExistsException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, Map.of("email", email));
    }
}
