package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(UUID id,
                                       Instant createdAt,
                                       String fileName,
                                       String contentType,
                                       byte[] bytes) {
    public static BinaryContentResponseDto of(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getCreatedAt(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getContent()
        );
    }
}
