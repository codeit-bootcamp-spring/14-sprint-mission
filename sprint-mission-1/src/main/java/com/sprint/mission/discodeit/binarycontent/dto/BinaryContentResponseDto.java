package com.sprint.mission.discodeit.binarycontent.dto;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentResponseDto(UUID binaryContentId, String fileName, String contentType,
                                       byte[] bytes) {

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
            binaryContent.getId(),
            binaryContent.getFileName(),
            binaryContent.getContentType(),
            binaryContent.getBytes()
        );
    }
}
