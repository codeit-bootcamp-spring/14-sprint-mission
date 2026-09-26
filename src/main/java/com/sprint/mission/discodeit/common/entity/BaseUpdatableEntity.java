package com.sprint.mission.discodeit.common.entity;

import com.sprint.mission.discodeit.common.entity.base.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {

    @LastModifiedDate
    private Instant updatedAt;

    public BaseUpdatableEntity(){
        super();
    }

    public void updateUpdatedAt(Instant updatedAt){
        this.updatedAt = updatedAt;
    }
}
