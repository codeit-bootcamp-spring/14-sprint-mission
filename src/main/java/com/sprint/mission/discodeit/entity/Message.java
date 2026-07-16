package com.sprint.mission.discodeit.entity;

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

    public void update(Message message) {
        this.content = message.getContent();
        this.updatedAt = now();
    }

    private Long now() {
        return System.currentTimeMillis();
    }


}
