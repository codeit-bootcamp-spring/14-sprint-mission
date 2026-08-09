package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentCreateRequestDto(
        byte[] data
) {
    public BinaryContent toEntity() {
        return new BinaryContent(data);
    }
}
