package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class ReadStatus extends BaseEntity{
    private UUID userId;
    private UUID channeId;

    protected ReadStatus(UUID id,Integer userNum, UUID userId, UUID channeId) {
        super(id, userNum);
        this.userId = userId;
        this.channeId = channeId;
    }

}
