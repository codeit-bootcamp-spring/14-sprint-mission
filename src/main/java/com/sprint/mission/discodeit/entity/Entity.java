package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public abstract class Entity implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    protected Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    protected void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }
}
