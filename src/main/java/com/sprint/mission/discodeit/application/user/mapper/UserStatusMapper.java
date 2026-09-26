package com.sprint.mission.discodeit.application.user.mapper;

import com.sprint.mission.discodeit.application.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.domain.UserStatus;
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
