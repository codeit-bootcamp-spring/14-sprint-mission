package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    // 직렬화
    // 외부에서 접근할 수 없고, 클래스 하나만 가지고, 상수이어야 하는 직렬화
    @Serial
    private static final long serialVersionUID = 1L;

    // 각 Entity의 공통 필드
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    protected void updateTime() {
        this.updatedAt = System.currentTimeMillis();
    }
}
