package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResponseDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponseDto binaryContentCreate(
        BinaryContentCreateRequestDto binaryContentCreateRequestDto);

    void binaryContentDelete(UUID binaryContentId);

    List<BinaryContentResponseDto> findAll();

    BinaryContentResponseDto findBinaryContent(UUID binaryContentId);
}
