package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class WrongPasswordException extends AuthException {

  public WrongPasswordException(String username) {
    super(ErrorCode.WRONG_PASSWORD, Map.of("username", username));
  }

}
