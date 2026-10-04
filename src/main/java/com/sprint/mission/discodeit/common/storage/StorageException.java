package com.sprint.mission.discodeit.common.storage;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class StorageException extends CustomException {
    public StorageException(StorageExceptionType type) {
        super(type);
    }
}
