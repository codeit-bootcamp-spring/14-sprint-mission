package com.sprint.mission.discodeit.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
public abstract class BaseEntity {
    @CreatedDate
    protected final Instant createdAt;

    protected BaseEntity() {
        this.createdAt = Instant.now();
    }
}
