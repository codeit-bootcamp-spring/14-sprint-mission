package com.sprint.mission.discodeit.core.domain.binaryContent;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;

public final class BinaryContentException extends DomainException {
    public BinaryContentException(BinaryContentExceptionType type, Map<String, Object> details) {
        super(type, details);
    }
}
