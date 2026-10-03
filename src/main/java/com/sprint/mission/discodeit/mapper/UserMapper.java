package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserDto toDto(User user, Boolean online) {
    return UserDto.toDto(user, online);
  }
}
