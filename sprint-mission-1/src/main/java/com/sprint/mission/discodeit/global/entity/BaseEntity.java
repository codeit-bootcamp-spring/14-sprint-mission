package com.sprint.mission.discodeit.global.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt;

    public void markUpdated() {
        this.updatedAt = Instant.now();
    }

    public void updateAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
