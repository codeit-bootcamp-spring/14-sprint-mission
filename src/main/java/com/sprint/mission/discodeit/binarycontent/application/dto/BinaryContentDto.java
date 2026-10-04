package com.sprint.mission.discodeit.binarycontent.application.dto;

import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

}
