package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BasicEntity {

    protected final UUID id;
    protected final Long createdAt;
    protected Long updatedAt;

    public BasicEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return this.id;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public Long getUpdatedAt() {
        return this.updatedAt;
    }
}
