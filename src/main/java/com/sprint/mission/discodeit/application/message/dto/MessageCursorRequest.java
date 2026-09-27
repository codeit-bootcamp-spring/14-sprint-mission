package com.sprint.mission.discodeit.application.message.dto;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageCursorRequest(
    UUID channelId,
    Instant cursor,
    UUID idAfter,
    Integer size
) {

  public MessageCursorRequest {
    if (size == null) {
      size = 50;
    }
    if (size < 1) {
      throw new DiscodeitRuntimeException(ExceptionType.INVALID_INFO);
    }
  }
}
