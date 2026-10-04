package com.sprint.mission.discodeit.binarycontent.application.provided.query;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentFinder {

  BinaryContentDto getById(UUID binaryContentId);

  List<BinaryContentDto> getAllByIds(List<UUID> binaryContentIds);
}
