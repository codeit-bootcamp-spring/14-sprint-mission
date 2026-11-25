package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;

public record Password(
    String password
) {

  public Password {
    if (password == null || password.isBlank()) {
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }
  }

  public static Password to(String password) {
    return new Password(password);
  }
}
