package com.sprint.mission.discodeit.adapter.in.controller.multipart;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class MultiPartFileException extends CustomException {
    public MultiPartFileException(MultiPartFileExceptionType type) {
        super(type);
    }
}
