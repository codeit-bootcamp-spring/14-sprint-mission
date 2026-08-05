package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    // 직렬화
    private static final long serialVersionID = 1L;

    private UUID id;

    // 메시지 내용, 사용자 ID, 채널 ID
    private String content;
    private UUID userId;
    private UUID channelId;

    private Long createdAt;
    private Long updatedAt;

    // 생성자
    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    // update()
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Message{id=" + this.id + ", content=" + this.content + ", userId=" + this.userId + ", channelId=" + this.channelId + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + "}";
    }
}
