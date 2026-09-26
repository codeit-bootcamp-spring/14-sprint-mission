package com.sprint.mission.discodeit.application.user.provided.command;

import com.sprint.mission.discodeit.application.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.application.user.dto.UserStatusUpdateRequest;
import java.util.UUID;

public interface UserStatusCommand {

  UserStatusDto update(UUID userId, UserStatusUpdateRequest request);
}
