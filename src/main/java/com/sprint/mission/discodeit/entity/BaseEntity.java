package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public BaseEntity(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId(){
        return this.id;
    }

    public Long getCreatedAt(){
        return this.createdAt;
    }

    public Long getUpdatedAt(){
        return this.updatedAt;
    }

    public void updateId(UUID id){
        this.id = id;
    }

    public void updateCreatedAt(Long createdAt){
        this.createdAt = createdAt;
    }

    public void updateUpdatedAt(Long updatedAt){
        this.updatedAt = updatedAt;
    }
}
