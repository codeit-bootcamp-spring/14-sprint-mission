package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * 공통 필드(id, createdAt, updatedAt)를 담는 상위 타입.
 * 시간은 Instant로 통일했다. Long(epoch)과 달리 시간대 변환과 기간 연산을 타입이 제공한다.
 */
public class Common implements Serializable {
    private static final long serialVersionUID = 2L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void update() {
        this.updatedAt = Instant.now();
    }

}
