package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@ToString
@Getter
public class Message implements Serializable {
    @ToString.Include
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    @ToString.Include
    private String content;
    @ToString.Include
    private final UUID userId;

    public Message(String content, UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;

        this.content = content;
        this.userId = userId;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = now();
    }

    private Long now() {
        return System.currentTimeMillis();
    }


}
