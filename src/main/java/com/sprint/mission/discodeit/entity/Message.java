package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends BasicEntity {

    private String text;
    private UUID user_id;
    private UUID channel_id;
    private List<UUID> attachmentIds;

    public Message(String text, UUID user_id, UUID channel_id, List<UUID> attachmentIds) {
        super();
        this.text = text;
        this.user_id = user_id;
        this.channel_id = channel_id;
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
            "', channel='" + channel_id +
            "', user='" + user_id +
            "'}";
    }
}
