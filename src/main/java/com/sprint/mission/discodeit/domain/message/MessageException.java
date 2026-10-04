package com.sprint.mission.discodeit.domain.message;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class MessageException extends CustomException {
    public MessageException(MessageExceptionType type) {
        super(type);
    }
}
