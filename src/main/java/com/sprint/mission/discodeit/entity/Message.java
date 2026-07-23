package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.message.MessageUpdateDto;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String content;
    private final UUID userId;

    public Message(String content, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;

        this.content = content;
        this.userId = userId;
    }

    public void update(MessageUpdateDto dto) {
        this.content = dto.getContent();
        this.updatedAt = now();
    }

    private Long now() {
        return System.currentTimeMillis();
    }


}
