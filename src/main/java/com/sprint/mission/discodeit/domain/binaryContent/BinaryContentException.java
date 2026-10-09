package com.sprint.mission.discodeit.domain.binaryContent;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class BinaryContentException extends CustomException {
    public BinaryContentException(BinaryContentExceptionType type) {
        super(type);
    }
}
