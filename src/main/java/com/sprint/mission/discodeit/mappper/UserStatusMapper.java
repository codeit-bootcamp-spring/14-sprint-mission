package com.sprint.mission.discodeit.mappper;


import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus status) {
    return new UserStatusDto(
        status.getId(),
        status.getUser().getId(),
        status.getLastActiveAt()
    );
  }
}
