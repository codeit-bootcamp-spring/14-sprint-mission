package com.sprint.mission.discodeit.binarycontent.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BinaryContent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();

    private final String fileName;
    private final String contentType;
    private final byte[] bytes;

    private long size;
}
