package com.sprint.mission.discodeit.common.exception.channel;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import java.util.Map;
import org.springframework.core.codec.CodecException;

public class ChannelException extends DiscodeitException {

    public ChannelException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
