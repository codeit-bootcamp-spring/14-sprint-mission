package com.sprint.mission.discodeit.common.exception;

import java.util.Map;

public class InvalidUserInputException extends UserException {

    public InvalidUserInputException(String field, String reason) {
        super(ErrorCode.INVALID_USER_INPUT, Map.of("field", field, "reason", reason));
    }
}
