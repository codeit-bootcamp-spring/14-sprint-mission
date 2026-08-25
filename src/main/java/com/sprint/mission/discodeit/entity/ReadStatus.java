package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends Basic{
    private UUID userId;
    private UUID channelId; //각 채널별 읽지않은 메시지를 참조
    private Instant lastReadAt;

    public ReadStatus(UUID id, UUID userId, UUID channelId) {
        super(id);
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void readChannel() {
        this.lastReadAt = Instant.now();
        super.updatedAt = Instant.now();
    }
}
