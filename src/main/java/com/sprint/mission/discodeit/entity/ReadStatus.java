package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus extends BasicEntity{
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;


    public void update(Instant lastReadAt) {
        if (lastReadAt != null) {
            this.lastReadAt = lastReadAt;
        }
    }

    public  ReadStatus(UUID userId, UUID channelId, Instant lastReadAt){
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

}
