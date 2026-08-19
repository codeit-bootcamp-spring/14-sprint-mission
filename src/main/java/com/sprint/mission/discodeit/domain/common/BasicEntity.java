package com.sprint.mission.discodeit.domain.common;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BasicEntity implements Identifiable {
    protected final UUID id;
    protected final Instant createdAt;

    protected BasicEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = now();
    }


    protected Instant now() {
        return Instant.now();
    }

}
