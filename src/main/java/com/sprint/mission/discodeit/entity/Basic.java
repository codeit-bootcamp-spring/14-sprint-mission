package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Basic implements Serializable {
    private static final long serialVersionUID = 1L;
    protected UUID id;
    protected Instant createdAt;
    protected Instant updatedAt;

    public Basic(UUID id) {
        this.id = id;
        this.createdAt = Instant.now();
        this.updatedAt = null;
    }

}
