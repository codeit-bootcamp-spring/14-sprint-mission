package com.sprint.mission.discodeit.common.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitRuntimeException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Instant timestamp;
  private final Map<String, Object> details;

  public DiscodeitRuntimeException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.timestamp = Instant.now();
    this.details = new HashMap<>();
    this.errorCode = errorCode;
  }

  public DiscodeitRuntimeException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
    this.details = new HashMap<>();
    this.timestamp = Instant.now();
  }

  public DiscodeitRuntimeException addDetails(String key, Object value) {
    this.details.put(key, value);
    return this;
  }


}
