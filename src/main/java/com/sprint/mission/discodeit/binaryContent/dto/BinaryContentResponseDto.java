package com.sprint.mission.discodeit.binaryContent.dto;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        Instant createdAt,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent){
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
