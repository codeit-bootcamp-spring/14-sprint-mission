package com.sprint.mission.discodeit.channel.application.required;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

  List<Channel> findAllByType(ChannelType type);

  List<Channel> findAllByUserId(UUID userId);

  Optional<Channel> findById(UUID channelId);

  Channel save(Channel channel);

  void delete(Channel channel);
}
