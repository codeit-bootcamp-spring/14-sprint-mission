package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class UpdatableEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    Instant updatedAt;

    protected UpdatableEntity() {
        super();
        this.updatedAt = this.createdAt;
    }

    protected void updateTimeStamp() {
        this.updatedAt = Instant.now();
    }


}