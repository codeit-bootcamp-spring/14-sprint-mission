package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends Common {
    private static final long serialVersionUID = 1L;

    private String content;
    private final UUID channelId;
    private final UUID authorId;

    public Message(
            String content,
            UUID channelId,
            UUID authorId
    ) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    public void update(String content) {
        this.content = content;
        update();
    }

    public String getContent() {
        return content;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String toString() {
        return String.format("내용: %s", content);
    }
}
