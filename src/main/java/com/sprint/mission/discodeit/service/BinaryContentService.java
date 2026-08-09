package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentResponseDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponseDto createBinaryContent(BinaryContentCreateRequestDto requestDto);

    BinaryContentResponseDto readBinaryContent(UUID id);

    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> ids);

    void deleteBinaryContent(UUID id);
}
