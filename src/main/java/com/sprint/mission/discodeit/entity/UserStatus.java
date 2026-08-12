package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    public UserStatus(UUID userId, Instant lastLoginAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastLoginAt = lastLoginAt;
    }

    public void update(Instant newLastLoginAt) {
        boolean anyValueUpdated = false;
        if(newLastLoginAt != null && !newLastLoginAt.equals(lastLoginAt)) {
            this.lastLoginAt = newLastLoginAt;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public boolean isOnline() {
        if(this.lastLoginAt == null) {
            return false;
        }
        return ChronoUnit.MINUTES.between(this.lastLoginAt, Instant.now()) < 5;
    }
}