package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Entity implements Identifiable {
    protected final UUID id;
    protected final Long createdAt;
    protected Long updatedAt;

    protected Entity() {
        this.id = UUID.randomUUID();
        this.createdAt = now();
        this.updatedAt = this.createdAt;
    }

    protected Long now() {
        return System.currentTimeMillis();
    }

}
