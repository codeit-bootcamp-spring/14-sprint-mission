package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCtreateRequestDto;
import com.sprint.mission.discodeit.dto.BinaryContentResponseDto;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentResponseDto create(BinaryContentCtreateRequestDto request);
    BinaryContentResponseDto find(UUID id);
    BinaryContentResponseDto findAllByIdIn(UUID id);
    BinaryContentResponseDto delete(UUID id);

}
