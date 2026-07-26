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
    private Long createdAt;
    private Long updatedAt;

    // 메시지 내용, 사용자 ID, 채널 ID
    @Setter
    private String content;
    private UUID userId;
    private UUID channelId;

    // 생성자
    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
    }

    // update()
    public void update(String content) {
        this.updatedAt = System.currentTimeMillis();
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{id=" + this.id + ", content=" + this.content + ", userId=" + this.userId + ", channelId=" + this.channelId + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + "}";
    }
}
