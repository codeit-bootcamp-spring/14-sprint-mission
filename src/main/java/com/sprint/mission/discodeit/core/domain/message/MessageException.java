package com.sprint.mission.discodeit.core.domain.message;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;

public final class MessageException extends DomainException {
    public MessageException(MessageExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
