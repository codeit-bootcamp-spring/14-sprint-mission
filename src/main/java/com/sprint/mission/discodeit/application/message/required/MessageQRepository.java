package com.sprint.mission.discodeit.application.message.required;

import com.sprint.mission.discodeit.domain.Message;
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
