package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record BinaryContentResponseDTO(
    UUID id,
    Instant createdAt,
    String fileName,
    Integer size,
    String contentType,
    String bytes
) {
    public static BinaryContentResponseDTO of(BinaryContent binaryContent, Integer size, byte[] bytes){
        String base64Bytes = (bytes != null) ? Base64.getEncoder().encodeToString(bytes) : null;

        return new BinaryContentResponseDTO(
            binaryContent.getId(),
            binaryContent.getCreatedAt(),
            binaryContent.getFileName(),
            size,
            binaryContent.getFileType(),
            base64Bytes
        );
    }
}
