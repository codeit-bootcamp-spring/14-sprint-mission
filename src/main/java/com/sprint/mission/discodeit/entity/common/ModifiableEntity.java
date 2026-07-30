package com.sprint.mission.discodeit.entity.common;

import java.time.Instant;

public abstract class ModifiableEntity extends BasicEntity
        implements Modifiable{
    private Instant updatedAt;

    public ModifiableEntity() {
        super();
        this.updatedAt = super.createdAt;
    }

    @Override
    public void markedAsUpdate() {
        updatedAt = super.now();
    }
}
