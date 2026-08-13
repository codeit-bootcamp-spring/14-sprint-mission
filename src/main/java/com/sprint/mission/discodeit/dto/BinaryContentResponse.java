package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse (
        UUID id,
        Instant createdAt,
        byte[] bytes,
        String fileName,
        String contentType
) { }
