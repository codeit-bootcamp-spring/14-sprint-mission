package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService extends BasicService<Channel>{
    ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto);

    void update(ChannelUpdateRequestDto request);

    ChannelResponseDto find(UUID id);
    List<ChannelResponseDto> findAllByUserId(UUID userId);

}
