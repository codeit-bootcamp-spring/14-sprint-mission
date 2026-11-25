package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserAlreadyExistException extends UserException{
    public UserAlreadyExistException(String field, Object value) {
        super(ErrorCode.DUPLICATE_USER, Map.of(field, value));
    }

}
