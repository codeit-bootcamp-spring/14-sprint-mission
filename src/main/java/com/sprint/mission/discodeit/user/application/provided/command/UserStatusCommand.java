package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.user.application.dto.UserStatusDto;

public interface UserStatusCommand {

  UserStatusDto update(RefreshUserStatusCommand command);
}
