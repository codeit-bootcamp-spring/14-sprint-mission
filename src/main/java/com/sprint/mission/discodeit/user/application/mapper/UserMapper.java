package com.sprint.mission.discodeit.user.application.mapper;

import com.sprint.mission.discodeit.binarycontent.application.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }

    UserStatus userStatus = user.getUserStatus();

    boolean online = userStatus != null && userStatus.isOnline();

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        binaryContentMapper.toDto(user.getProfile()),
        online);
  }
}
