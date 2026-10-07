package com.sprint.mission.discodeit.dto.userStatusDto;

import com.sprint.mission.discodeit.domain.UserStatus;

public record UserStatusResponse(
    boolean isOnline
) {

  public static UserStatusResponse from(UserStatus userStatus) {
    return new UserStatusResponse(userStatus.isOnline());
  }
}
