package com.sprint.mission.discodeit.channel.application;

import com.sprint.mission.discodeit.channel.dto.*;
import com.sprint.mission.discodeit.common.application.BasicService;
import com.sprint.mission.discodeit.channel.domain.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService extends BasicService<Channel> {
    ChannelResponseDto publicCreate(PublicChannelCreateRequest channelCreateRequestDto);

    ChannelResponseDto privateCreate(PrivateChannelCreateRequest channelCreateRequestDto);

    ChannelResponseDto update(UUID id, ChannelUpdateRequestDto request);

    ChannelFindResponseDto find(UUID id);
    List<ChannelFindResponseDto> findAllByUserId(UUID userId);

}
