package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;

public record UserResponse(
    String name,
    String email,
    boolean isOnline
) {

  public static UserResponse from(User user, UserStatus userStatus) {
    return new UserResponse(user.getName(), user.getEmail(), userStatus.isOnline());
  }
}
