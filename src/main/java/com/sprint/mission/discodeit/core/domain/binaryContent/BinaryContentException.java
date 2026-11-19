package com.sprint.mission.discodeit.core.domain.binaryContent;

import com.sprint.mission.discodeit.core.common.exception.CustomException;

import java.util.Map;

public final class BinaryContentException extends CustomException {
    public BinaryContentException(BinaryContentExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
