package com.sprint.mission.discodeit.core.domain.user;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;

public final class UserException extends DomainException {

    public UserException(UserExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
