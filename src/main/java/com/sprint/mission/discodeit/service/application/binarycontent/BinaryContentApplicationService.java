package com.sprint.mission.discodeit.service.application.binarycontent;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentApplicationService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequest);
    BinaryContentResponseDto findById(UUID binaryContentId);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
}
