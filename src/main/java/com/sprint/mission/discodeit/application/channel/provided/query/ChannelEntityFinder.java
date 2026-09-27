package com.sprint.mission.discodeit.application.channel.provided.query;

import com.sprint.mission.discodeit.domain.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelEntityFinder {

  Channel getEntityById(UUID channelId);

  List<Channel> getAllByUserId(UUID userId);
}
