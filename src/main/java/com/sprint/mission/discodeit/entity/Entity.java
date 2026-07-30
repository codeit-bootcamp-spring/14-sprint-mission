package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class Entity implements Identifiable {
    protected final UUID id;
    protected final Instant createdAt;
    protected Instant updatedAt;

    protected Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = now();
        this.updatedAt = this.createdAt;
    }

    protected void markAsUpdate() {
        updatedAt = now();
    }

    private Instant now() {
        return Instant.now();
    }

}
