package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.MessageDto;
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
    private final User user;

    public Message(String content, User user) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;

        this.content = content;
        this.user = user;
    }

    public void update(MessageDto dto) {
        this.content = dto.getContent();
        this.updatedAt = now();
    }

    private Long now() {
        return System.currentTimeMillis();
    }


}
