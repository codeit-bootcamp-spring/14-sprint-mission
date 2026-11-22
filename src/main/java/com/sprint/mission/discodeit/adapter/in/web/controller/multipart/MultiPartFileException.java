package com.sprint.mission.discodeit.adapter.in.web.controller.multipart;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;

public final class MultiPartFileException extends DomainException {
    public MultiPartFileException(MultiPartFileExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
