package com.sprint.mission.discodeit.binarycontent.application;

import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import org.springframework.core.io.Resource;

public record BinaryContentDownload(
    Resource resource,
    String fileName,
    long size,
    String contentType
) {
    static public BinaryContentDownload of(BinaryContent binaryContent, Resource resource){
        return new BinaryContentDownload(
            resource,
            binaryContent.getFileName(),
            binaryContent.getSize(),
            binaryContent.getContentType()
        );
    }
}
