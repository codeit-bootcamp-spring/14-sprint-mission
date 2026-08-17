package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class Message extends Common {
    private static final long serialVersionUID = 2L;

    private String content;
    private final UUID channelId;
    private final UUID authorId;
    /** 첨부한 BinaryContent의 id들. 첨부가 바뀌는 게 아니라 메시지 자체가 지워지고 다시 만들어지는 개념이라 불변으로 둔다. */
    private final List<UUID> attachmentIds;

    public Message(
            String content,
            UUID channelId,
            UUID authorId,
            List<UUID> attachmentIds
    ) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds == null ? List.of() : List.copyOf(attachmentIds);
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

    public List<UUID> getAttachmentIds() {
        return attachmentIds;
    }

    public String toString() {
        return String.format("내용: %s", content);
    }
}
