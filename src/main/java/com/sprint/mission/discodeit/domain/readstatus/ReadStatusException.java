package com.sprint.mission.discodeit.domain.readstatus;

import com.sprint.mission.discodeit.common.exception.CustomException;


public final class ReadStatusException extends CustomException {
    public ReadStatusException(ReadStatusExceptionType type) {
        super(type);
    }
}
