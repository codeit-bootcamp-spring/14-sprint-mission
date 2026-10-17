package com.sprint.mission.discodeit.binarycontent.web.dto.res;

import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record BinaryContentResponseDTO(
    UUID id,
    String fileName,
    long size,
    String contentType
) {}
