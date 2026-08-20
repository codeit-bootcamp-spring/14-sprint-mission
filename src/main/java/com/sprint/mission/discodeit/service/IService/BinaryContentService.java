package com.sprint.mission.discodeit.service.IService;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto request);
    BinaryContentResponseDto find(UUID id);
    BinaryContentResponseDto findAllByIdIn(UUID id);
    BinaryContentResponseDto delete(UUID id);

}
