package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository{
    Channel channelAdd(Channel channel);
    Optional<Channel> findByChannel(String channelName);
    void delete(Channel channel);
    List<Channel> findAllChannel();
}
