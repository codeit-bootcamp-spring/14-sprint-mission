package com.sprint.mission.discodeit.application.message.provided.query;

import com.sprint.mission.discodeit.domain.Message;
import java.util.UUID;

public interface MessageEntityFinder {

  Message getEntityById(UUID messageId);


}
