package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelDto createPublic(PublicChannelCreateRequest request);
    ChannelDto createPrivate(PrivateChannelCreateRequest request);

    Optional<ChannelDto> find(UUID id);
    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto update(UUID id, ChannelUpdateRequest request);
    void delete(UUID id);
}
