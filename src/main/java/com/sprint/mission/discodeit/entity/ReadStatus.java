package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
/*
사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다.
사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
 */
public class ReadStatus {
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public ReadStatus (UUID userId, UUID channelId) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void setLastReadAt() {
        this.lastReadAt = Instant.now();
        setUpdatedAt();
    }
}
