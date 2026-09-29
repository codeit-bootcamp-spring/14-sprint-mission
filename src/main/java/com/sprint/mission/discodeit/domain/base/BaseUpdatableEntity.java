package com.sprint.mission.discodeit.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

public abstract class BaseUpdatableEntity extends BaseEntity {
    @Getter
    @LastModifiedDate
    private Instant updatedAt;

    public BaseUpdatableEntity() {
        super();
        this.updatedAt = super.createdAt;
    }

}
