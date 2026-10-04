package com.sprint.mission.discodeit.readstatus.application.provided.command;

import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusUpdateRequest;
import java.util.UUID;

public interface ReadStatusModifier {

  ReadStatusDto modify(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);
}
