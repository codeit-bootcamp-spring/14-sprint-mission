package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class UserStatus extends BaseEntity {
    private UUID userId;

    protected UserStatus(UUID id, Integer userNum, UUID userId) {
        super(id, userNum);
        this.userId = userId;
    }
}
