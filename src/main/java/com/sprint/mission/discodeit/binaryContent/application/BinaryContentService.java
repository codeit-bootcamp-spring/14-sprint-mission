package com.sprint.mission.discodeit.binaryContent.application;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentResponseDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto request);
    BinaryContentResponseDto find(UUID id);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);
}
