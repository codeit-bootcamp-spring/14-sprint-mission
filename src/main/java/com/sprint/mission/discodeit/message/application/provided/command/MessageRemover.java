package com.sprint.mission.discodeit.message.application.provided.command;

import java.util.UUID;

public interface MessageRemover {

  void remove(UUID messageId);

  void removeAllByChannelId(UUID channelId);

}
