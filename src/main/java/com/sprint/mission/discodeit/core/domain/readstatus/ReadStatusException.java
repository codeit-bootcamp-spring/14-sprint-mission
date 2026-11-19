package com.sprint.mission.discodeit.core.domain.readstatus;

import com.sprint.mission.discodeit.core.common.exception.CustomException;

import java.util.Map;


public final class ReadStatusException extends CustomException {
    public ReadStatusException(ReadStatusExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
