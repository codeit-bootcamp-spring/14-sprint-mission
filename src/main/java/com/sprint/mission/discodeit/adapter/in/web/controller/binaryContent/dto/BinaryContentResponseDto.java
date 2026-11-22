package com.sprint.mission.discodeit.adapter.in.web.controller.binaryContent.dto;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentResult;

import java.util.Objects;
import java.util.UUID;

public record BinaryContentResponseDto(UUID id,
                                       String fileName,
                                       Long size,
                                       String contentType) {
    public static BinaryContentResponseDto from(BinaryContentResult result) {
        if (Objects.isNull(result)) {
            return null;
        }
        return new BinaryContentResponseDto(
                result.id(),
                result.filename(),
                result.size(),
                result.contentType()
        );
    }
}
