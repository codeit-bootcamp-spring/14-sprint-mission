package com.sprint.mission.discodeit.user.application.required;

import com.sprint.mission.discodeit.user.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

  Optional<UserStatus> findByUser_Id(UUID userId);
}
