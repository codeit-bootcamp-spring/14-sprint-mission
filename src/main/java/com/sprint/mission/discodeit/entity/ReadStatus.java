package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

/** 사용자가 채널별로 마지막으로 메시지를 읽은 시간. 안 읽은 메시지를 계산하는 데 쓴다. */
public class ReadStatus extends Common {
    private static final long serialVersionUID = 1L;

    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public Instant getLastReadAt() {
        return lastReadAt;
    }

    public void updateLastReadAt(Instant newLastReadAt) {
        this.lastReadAt = newLastReadAt;
        update();
    }

}
