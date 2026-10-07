package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public abstract class UserStatusException extends DiscodeitException {

  protected UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode, details);
  }

  protected UserStatusException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
    super(errorCode, details, cause);
  }
}
