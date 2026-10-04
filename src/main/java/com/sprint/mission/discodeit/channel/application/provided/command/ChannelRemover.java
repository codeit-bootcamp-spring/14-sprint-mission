package com.sprint.mission.discodeit.channel.application.provided.command;

import java.util.UUID;

public interface ChannelRemover {

  void delete(UUID channelId);
}
