package com.sprint.mission.discodeit.adapter.in.controller.dto.binaryContent;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;

import java.util.List;
import java.util.UUID;

public record BinaryContentResponseDto(UUID id,
                                       String fileName,
                                       Long size,
                                       String contentType) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );
    }

    public static List<BinaryContentResponseDto> from(List<BinaryContent> binaryContents) {
        return binaryContents.stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }
}
