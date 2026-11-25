package com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response;

import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;

import java.util.Objects;
import java.util.UUID;

public record BinaryContentResult(UUID id,
                                  String filename,
                                  Long size,
                                  String contentType) {

    public static BinaryContentResult from(BinaryContent entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return new BinaryContentResult(
                entity.getId(),
                entity.getFileName(),
                entity.getSize(),
                entity.getContentType()
        );
    }
}
