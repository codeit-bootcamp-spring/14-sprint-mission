package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import java.util.regex.Pattern;

public record Email(String value) {

  private static final Pattern FORMAT =
      Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

  public Email {
    if (value == null || value.isBlank()) {
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }

    if (!FORMAT.matcher(value).matches()) {
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }

    if (value.length() > 100) {
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }

  }

  public static Email to(String value) {
    return new Email(value);
  }
}
