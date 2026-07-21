package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BasicEntity {

    private String text;
    private UUID user_id;
    private UUID channel_id;

    public Message(String text, UUID user_id, UUID channel_id) {
        super();
        this.text = text;
        this.user_id = user_id;
        this.channel_id = channel_id;
    }


    public UUID getUser_id() {
        return user_id;
    }

    public UUID getChannel_id() {
        return channel_id;
    }

    public String getText() {
        return this.text;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setText(String text) {
        this.text = text;
        this.updatedAt = System.currentTimeMillis();
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
