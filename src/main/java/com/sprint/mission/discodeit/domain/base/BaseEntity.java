package com.sprint.mission.discodeit.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity {
    protected final UUID id;
    @CreatedDate
    protected final Instant createdAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
