package com.sprint.mission.discodeit.entity.common;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BasicEntity implements Identifiable {
    protected final UUID id;
    protected final Instant createdAt;
    protected Instant updatedAt;

    protected BasicEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = now();
        this.updatedAt = this.createdAt;
    }


    protected Instant now() {
        return Instant.now();
    }

}
