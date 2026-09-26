package com.sprint.mission.discodeit.application.readstatus.provided.query;

import com.sprint.mission.discodeit.domain.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusEntityFinder {

  ReadStatus getEntityById(UUID readStatusID);

  List<ReadStatus> getEntitiesByUserId(UUID userId);

  List<ReadStatus> getEntitiesByChannelId(UUID channelId);
}
