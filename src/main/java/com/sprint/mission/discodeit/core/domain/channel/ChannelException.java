package com.sprint.mission.discodeit.core.domain.channel;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;

public final class ChannelException extends DomainException {
    public ChannelException(ChannelExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
