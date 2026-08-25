package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends Basic{
    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID id, UUID userId) {
        super(id);
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public void updateActive() {
        this.lastActiveAt = Instant.now();
        super.updatedAt = Instant.now();
    }

    public boolean isOnline() {

        if (this.lastActiveAt.isAfter(Instant.now().minusSeconds(300))) {
            return true;
        }
        else {
            return false;
        }

    }
}
