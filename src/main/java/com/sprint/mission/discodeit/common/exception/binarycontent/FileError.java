package com.sprint.mission.discodeit.common.exception.binarycontent;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;

public class FileError extends DiscodeitException {

    public FileError() {
        super(ErrorCode.ERROR_FILES_STORE);
    }
}
