package com.sprint.mission.discodeit.service.IService;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import java.util.List;
import java.util.UUID;

public interface IBinaryContentService {
    BinaryContentResponseDto create(BinaryContentCreateRequestDto request);
    BinaryContentResponseDto find(UUID id);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);

}
