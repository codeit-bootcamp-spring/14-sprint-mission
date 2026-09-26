package com.sprint.mission.discodeit.application.readstatus.provided.command;

import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusUpdateRequest;
import java.util.UUID;

public interface ReadStatusModifier {

  ReadStatusDto modify(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);
}
