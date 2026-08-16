package com.sprint.mission.discodeit.binarycontent.entity;

import jakarta.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BinaryContent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    UUID binaryContentId = UUID.randomUUID();
    Instant createdAt = Instant.now();

    @Nullable
    private String fileName;
    private String contentType;
    private byte[] bytes;
}
