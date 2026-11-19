package com.sprint.mission.discodeit.adapter.in.controller.multipart;

import com.sprint.mission.discodeit.core.common.exception.CustomException;

import java.util.Map;

public final class MultiPartFileException extends CustomException {
    public MultiPartFileException(MultiPartFileExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
