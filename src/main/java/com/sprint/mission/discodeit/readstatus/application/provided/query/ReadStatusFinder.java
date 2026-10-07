package com.sprint.mission.discodeit.readstatus.application.provided.query;

import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusDto;
import java.util.List;
import java.util.UUID;

public interface ReadStatusFinder {

  List<ReadStatusDto> getByUserId(UUID userId);
}
