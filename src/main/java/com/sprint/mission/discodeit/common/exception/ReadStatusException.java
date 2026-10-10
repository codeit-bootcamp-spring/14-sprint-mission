package com.sprint.mission.discodeit.common.exception;

import java.util.Map;

public abstract class ReadStatusException extends DiscodeitException {

    protected ReadStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
