package com.sprint.mission.discodeit.adapter.out.storage;

import com.sprint.mission.discodeit.core.domain.base.DomainException;

import java.util.Map;

public final class StorageException extends DomainException {
    public StorageException(StorageExceptionType type, Map<String, Object> details) {
        super(type, details);
    }

    public StorageException(StorageExceptionType type, Map<String, Object> details, Exception cause) {
        super(type, details, cause);
    }
}
