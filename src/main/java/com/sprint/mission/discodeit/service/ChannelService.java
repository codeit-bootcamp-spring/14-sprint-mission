package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelUpdateNameDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(ChannelCreationDto dto);

    Optional<Channel> getChannel(UUID uuid);

    List<Channel> getAllChannels();

    void updateChannelName(UUID id, ChannelUpdateNameDto dto);

    void deleteChannel(UUID uuid);
}
