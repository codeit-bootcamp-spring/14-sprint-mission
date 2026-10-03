package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity{
    private UUID userId;
    private UUID channeId;
    private Instant lastReadChannelMessage;

    protected ReadStatus(UUID id,Integer userNum, UUID userId, UUID channeId) {
        super(id, userNum);
        this.userId = userId;
        this.channeId = channeId;
    }

    // 마지막 읽은 메세지 시간
    public Instant lastReadChannelMessage() {
        this.lastReadChannelMessage = Instant.now();
        return lastReadChannelMessage;
    }

}
