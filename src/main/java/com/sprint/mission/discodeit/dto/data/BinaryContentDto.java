package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentDto(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {
  public static BinaryContentDto toDto(BinaryContent binaryContent) {
    return new BinaryContentDto(
        binaryContent.getId(),
        binaryContent.getCreatedAt(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType(),
        binaryContent.getBytes()
    );
  }
}
