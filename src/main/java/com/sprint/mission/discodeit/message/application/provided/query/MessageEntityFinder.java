package com.sprint.mission.discodeit.message.application.provided.query;

import com.sprint.mission.discodeit.message.domain.Message;
import java.util.List;
import java.util.UUID;

public interface MessageEntityFinder {

  Message getEntityById(UUID messageId);

  List<Message> getEntitiesByChannelId(UUID channelId);
}
