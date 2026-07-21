package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.ChannelDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel create(Channel channel);

    Optional<Channel> findById(UUID id);

    List<Channel> findAll();

    void update(UUID id, ChannelDto dto);

    void deleteById(UUID id);
}
