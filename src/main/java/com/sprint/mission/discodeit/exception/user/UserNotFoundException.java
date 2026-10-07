package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  private UserNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_NOT_FOUND, details);
  }

  public static UserNotFoundException withId(UUID userId) {
    return new UserNotFoundException(Map.of("userId", userId));
  }

  public static UserNotFoundException withUsername(String username) {
    return new UserNotFoundException(Map.of("username", username));
  }

}
