package com.sprint.mission.discodeit.message.application.required;

import com.sprint.mission.discodeit.message.domain.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageQRepository {

  List<Message> findAllByCursor(
      UUID channelId,
      Instant cursor,
      UUID idAfter,
      int limit
  );
}
