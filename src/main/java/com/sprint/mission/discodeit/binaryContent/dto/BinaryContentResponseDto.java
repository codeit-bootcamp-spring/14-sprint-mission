package com.sprint.mission.discodeit.binaryContent.dto;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        byte[] data,
        Instant createdAt
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent){
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getData(),
                binaryContent.getCreatedAt()
        );
    }
}
