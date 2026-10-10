package com.sprint.mission.discodeit.common.exception;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(UUID binaryContentId) {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND, Map.of("binaryContentId", binaryContentId));
    }

    public BinaryContentNotFoundException(List<UUID> binaryContentIds) {
        super(ErrorCode.BINARY_CONTENT_NOT_FOUND, Map.of("binaryContentIds", binaryContentIds));
    }
}
