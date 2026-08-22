package com.sprint.mission.discodeit.common.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    public BaseEntity(){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void updateUpdatedAt(Instant updatedAt){
        this.updatedAt = updatedAt;
    }
}
