package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message extends BaseEntity {
    // 메시지 내용, 채널 ID, 글쓴이 ID
    private String content;
    private UUID channelId;
    private UUID authorId;

    // 생성자
    public Message(String content, UUID channelId, UUID authorId) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    // update()
    public void updateContent(String content) {
        this.content = content;
        super.updateTime();
    }

    @Override
    public String toString() {
        return "Message{id=" + super.getId() + ", content=" + this.content + ", channelId=" + this.channelId + ", authorId=" + this.authorId + ", createdAt=" + super.getCreatedAt() + ", updatedAt=" + super.getUpdatedAt() + "}";
    }
}
