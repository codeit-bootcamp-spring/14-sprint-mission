package com.sprint.mission.discodeit.application.channel.provided.command;

import java.util.UUID;

public interface ChannelRemover {

  void delete(UUID channelId);
}
