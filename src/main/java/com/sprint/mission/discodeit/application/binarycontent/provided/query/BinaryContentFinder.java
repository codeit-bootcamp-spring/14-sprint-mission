package com.sprint.mission.discodeit.application.binarycontent.provided.query;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentFinder {

  BinaryContentDto getById(UUID binaryContentId);

  List<BinaryContentDto> getAllByIds(List<UUID> binaryContentIds);
}
