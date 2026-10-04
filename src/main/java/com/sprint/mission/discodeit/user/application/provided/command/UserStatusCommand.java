package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.user.application.dto.UserStatusDto;
import com.sprint.mission.discodeit.user.application.dto.UserStatusUpdateRequest;
import java.util.UUID;

public interface UserStatusCommand {

  UserStatusDto update(UUID userId, UserStatusUpdateRequest request);
}
