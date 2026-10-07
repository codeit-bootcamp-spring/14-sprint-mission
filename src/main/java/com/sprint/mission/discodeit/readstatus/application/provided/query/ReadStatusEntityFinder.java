package com.sprint.mission.discodeit.readstatus.application.provided.query;

import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusEntityFinder {

  ReadStatus getEntityById(UUID readStatusID);

  List<ReadStatus> getEntitiesByUserId(UUID userId);

  List<ReadStatus> getEntitiesByChannelId(UUID channelId);
}
