package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class UserException extends CustomException {

    public UserException(UserExceptionType type) {
        super(type);
    }
}
