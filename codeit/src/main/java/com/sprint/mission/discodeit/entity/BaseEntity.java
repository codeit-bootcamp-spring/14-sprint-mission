package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUId = 1L;

    private UUID id;
    private Instant createdAt;
    private Integer createdBy;
    private Instant updatedAt;
    private Integer updatedBy;

    protected BaseEntity(UUID id, Integer userNum) {
        this.id = id;
        this.createdAt = Instant.now();
        this.createdBy = userNum;
        this.updatedAt = Instant.now();
        this.updatedBy = userNum;
    }

    protected void updated(Integer userNum) {
        this.updatedAt = Instant.now();
        this.updatedBy = userNum;
    }
}
