package com.sprint.mission.discodeit.file.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {
    void channelLoad();
    void channelFlush();
    List<String> readChannelName();
    List<Channel> channelAdd(List<Channel> channels);
    Optional<Channel> findByChannel(String channelName);
    void delete(Channel channel);
    List<Channel> findAllChannel();
}
