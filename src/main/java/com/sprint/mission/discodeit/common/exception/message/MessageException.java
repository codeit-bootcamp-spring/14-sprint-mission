package com.sprint.mission.discodeit.common.exception.message;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import java.util.Map;

public class MessageException extends DiscodeitException {

    public MessageException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
