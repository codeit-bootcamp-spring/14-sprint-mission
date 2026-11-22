package com.sprint.mission.discodeit.core.common.persistence;

public class NotFoundException extends PersistenceException {
    public NotFoundException(String message) {
        super(message);
    }
}
