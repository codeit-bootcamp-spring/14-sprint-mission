package com.sprint.mission.discodeit.domain.common;

import lombok.Getter;

import java.time.Instant;

public abstract class ModifiableEntity extends BasicEntity
        implements Modifiable{
    @Getter
    private Instant updatedAt;

    public ModifiableEntity() {
        super();
        this.updatedAt = super.createdAt;
    }

    @Override
    public void markedAsUpdate(Instant newUpdatedAt) {
        this.updatedAt = newUpdatedAt;
    }
}
