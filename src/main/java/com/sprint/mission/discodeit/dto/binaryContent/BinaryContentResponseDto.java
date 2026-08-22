package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import java.util.UUID;

public record BinaryContentResponseDto(UUID binaryContentId,
                                       byte[] content) {
    public static BinaryContentResponseDto of(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getContent()
        );
    }
}
