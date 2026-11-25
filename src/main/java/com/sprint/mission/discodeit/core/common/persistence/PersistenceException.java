package com.sprint.mission.discodeit.core.common.persistence;

public abstract class PersistenceException extends RuntimeException {
    public PersistenceException(String message) {
        super(message);
    }
}
