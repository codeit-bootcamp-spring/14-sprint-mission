package com.sprint.mission.discodeit.core.domain.readstatus;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;


public final class ReadStatusException extends DomainException {
    public ReadStatusException(ReadStatusExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
