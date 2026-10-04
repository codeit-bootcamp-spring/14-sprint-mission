package com.sprint.mission.discodeit.common.multipart;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class MultiPartFileException extends CustomException {
    public MultiPartFileException(MultiPartFileExceptionType type) {
        super(type);
    }
}
