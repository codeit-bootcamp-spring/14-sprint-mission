package com.sprint.mission.discodeit.binaryContent.dto;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;

public record BinaryContentCreateRequestDto(
        byte[] data
) {
    public BinaryContent toEntity() {
        return new BinaryContent(data);
    }
}
