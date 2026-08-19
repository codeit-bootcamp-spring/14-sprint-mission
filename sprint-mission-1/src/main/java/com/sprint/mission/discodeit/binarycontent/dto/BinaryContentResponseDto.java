package com.sprint.mission.discodeit.binarycontent.dto;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(UUID id, Instant createdAt, String fileName, long size,
                                       String contentType, byte[] bytes) {

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
            binaryContent.getId(),
            binaryContent.getCreatedAt(),
            binaryContent.getFileName(),
            binaryContent.getSize(),
            binaryContent.getContentType(),
            binaryContent.getBytes()
        );
    }
}
