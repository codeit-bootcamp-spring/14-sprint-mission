package com.sprint.mission.discodeit.user.application.provided.query;

import com.sprint.mission.discodeit.user.application.dto.UserDto;
import java.util.UUID;

public interface UserFinder {

  UserDto getById(UUID userId);
}
