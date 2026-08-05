package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BinaryContent {
    private final String contentAddress;
    private final UUID userId;
    private final UUID messageId;

    private final UUID id;
    private final Instant createdAt;

    public BinaryContent(String contentAddress, UUID userId, UUID messageId) {
        this.contentAddress = contentAddress;
        this.userId = userId;
        this.messageId = messageId;

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
