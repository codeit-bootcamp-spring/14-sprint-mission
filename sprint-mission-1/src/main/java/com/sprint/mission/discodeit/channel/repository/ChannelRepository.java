package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.channel.entity.Channel;

import com.sprint.mission.discodeit.channel.entity.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

    Channel channelAdd(Channel channel);

    Optional<Channel> findByChannel(UUID channelId);

    void delete(Channel channel);

    void update(Channel channel);

    List<Channel> findAllChannel();

    List<Channel> findAllByType(ChannelType channelType);
}
