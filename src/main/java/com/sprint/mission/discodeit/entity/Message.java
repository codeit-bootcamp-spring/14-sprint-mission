package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@ToString
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String values;
    private final UUID channelId;
    private final UUID senderId;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public Message(String values, UUID channelId, UUID senderId) {
        this.values = values;
        this.channelId = channelId;
        this.senderId = senderId;

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
