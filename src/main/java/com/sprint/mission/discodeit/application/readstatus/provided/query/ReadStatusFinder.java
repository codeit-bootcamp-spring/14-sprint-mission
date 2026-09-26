package com.sprint.mission.discodeit.application.readstatus.provided.query;

import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;
import java.util.List;
import java.util.UUID;

public interface ReadStatusFinder {

  List<ReadStatusDto> getByUserId(UUID userId);
}
