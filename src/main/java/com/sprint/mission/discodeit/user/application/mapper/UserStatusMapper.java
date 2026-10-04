package com.sprint.mission.discodeit.user.application.mapper;

import com.sprint.mission.discodeit.user.application.dto.UserStatusDto;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt()
    );
  }
}
