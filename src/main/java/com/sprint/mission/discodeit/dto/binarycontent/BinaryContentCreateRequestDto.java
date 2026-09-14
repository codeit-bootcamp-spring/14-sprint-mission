package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentCreateRequestDto(
    String fileName,
    String contentType,
    byte[] bytes
) {
    public BinaryContent toEntity() {
        return new BinaryContent(fileName, (long) bytes.length, contentType);
    }
}
