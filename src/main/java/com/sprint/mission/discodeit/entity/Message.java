package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.UUID;

@ToString
@Getter
public final class Message extends Entity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    private String content;
    @ToString.Include
    private final UUID userId;

    public Message(String content, UUID userId) {
        super();
        this.content = content;
        this.userId = userId;
    }

    public void updateContent(String content) {
        this.content = content;
        super.updatedAt = now();
    }
}
