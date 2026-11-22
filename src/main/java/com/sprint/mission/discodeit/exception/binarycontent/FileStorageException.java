package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class FileStorageException extends BinaryContentException {

    public FileStorageException(String target) {
        super(ErrorCode.FILE_STORAGE_ERROR, Map.of("target", target));
    }
}
