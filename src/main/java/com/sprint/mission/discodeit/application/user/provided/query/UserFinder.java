package com.sprint.mission.discodeit.application.user.provided.query;

import com.sprint.mission.discodeit.application.user.dto.UserDto;
import java.util.UUID;

public interface UserFinder {

  UserDto getById(UUID userId);
}
