package com.sprint.mission.discodeit.domain.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {
    @Getter
    @LastModifiedDate
    private Instant updatedAt;

    protected BaseUpdatableEntity() {
        super();
        this.updatedAt = super.createdAt;
    }

}
