package com.sprint.mission.discodeit.adapter.out.storage;

import com.sprint.mission.discodeit.common.exception.CustomException;

public final class StorageException extends CustomException {
    public StorageException(StorageExceptionType type) {
        super(type);
    }
}
