package com.sprint.mission.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

// 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용.
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userId;
    private final UUID channelId;

    private Instant lastReadAt;

    private ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = null;
    }

    public static ReadStatus create(UUID userId, UUID channelId) {
        return new ReadStatus(
                userId, channelId
        );
    }

    public void markAsRead() {
        this.lastReadAt = Instant.now();
        this.updatedAt = lastReadAt;
    }

    public ReadStatus copy() {
        return new ReadStatus(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.userId,
                this.channelId,
                this.lastReadAt
        );
    }
}
