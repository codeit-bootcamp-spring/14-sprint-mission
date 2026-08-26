package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    String contentType,
    long size,
    byte[] bytes
) {
    public static BinaryContentDto from(BinaryContent binaryContent){
        return new BinaryContentDto(
            binaryContent.getId(),
            binaryContent.getFileName(),
            binaryContent.getContentType(),
            binaryContent.getSize(),
            binaryContent.getBytes()
        );
    }

}
