package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends BasicEntity {

    private String text;
    private UUID userId;
    private UUID channelId;
    private List<UUID> attachmentIds;

    public Message(String text, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        super();
        this.text = text;
        this.userId = userId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }





    public void setText(String text) {
        this.text = text;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{id=" + id +
            ", createdAt='" + createdAt +
            "', updatedAt='" + updatedAt +
            "', text='" + text +
            "', channel='" + channelId +
            "', user='" + userId +
            "'}";
    }
}
