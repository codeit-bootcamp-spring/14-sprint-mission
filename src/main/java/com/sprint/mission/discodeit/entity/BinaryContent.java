package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private final byte[] data;

    public BinaryContent(byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = data;
    }
}
