package com.sprint.mission.discodeit.binaryContent.dto;

import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequestDto(
        MultipartFile data
) {

}
