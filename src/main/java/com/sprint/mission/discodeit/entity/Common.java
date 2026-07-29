package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Common {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public Common() {
        long now = System.currentTimeMillis();
        this.id = UUID.randomUUID();
        this.createdAt = now;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    protected void update() {
        this.updatedAt = System.currentTimeMillis();
    }

}
