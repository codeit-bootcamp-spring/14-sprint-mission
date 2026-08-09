package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String content;
    private UUID senderId;      // 메시지를 작성한 user의 id
    private UUID channelId;     // 메시지가 속한 channel의 id

    private List<UUID> attachmentIds;   // 첨부파일 리스트

    private Message(
            String content,
            UUID senderId,
            UUID channelId,
            List<UUID> attachmentIds
    ) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;

        if (Objects.isNull(attachmentIds)) {
            this.attachmentIds = List.of();
        } else {
            this.attachmentIds = List.copyOf(attachmentIds);
        }
    }


    public static Message create(
            String content,
            UUID senderId,
            UUID channelId,
            List<UUID> attachmentIds
    ) {
        return new Message(
                content, senderId, channelId, attachmentIds
        );
    }

    public void updateContent(String content) {
        if (Objects.nonNull(content) && !content.equals(this.content)) {
            this.content = content;
            this.updatedAt = Instant.now();
        }
    }
}
