package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private final String fileName;
    private final byte[] bytes;


    private BinaryContent(String fileName, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public static BinaryContent create(String fileName, byte[] bytes) {
        return new BinaryContent(
                fileName,
                bytes
        );
    }

    public byte[] getBytes() {
        return this.bytes.clone();
    }
}
