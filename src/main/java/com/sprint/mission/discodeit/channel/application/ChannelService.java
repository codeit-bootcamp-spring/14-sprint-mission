package com.sprint.mission.discodeit.channel.application;

import com.sprint.mission.discodeit.channel.dto.*;
import com.sprint.mission.discodeit.common.application.BasicService;
import com.sprint.mission.discodeit.channel.domain.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService extends BasicService<Channel> {
    ChannelDto publicCreate(PublicChannelCreateRequest channelCreateRequestDto);

    ChannelDto privateCreate(PrivateChannelCreateRequest channelCreateRequestDto);

    ChannelDto update(UUID id, ChannelUpdateRequestDto request);

    ChannelDto find(UUID id);
    List<ChannelDto> findAllByUserId(UUID userId);

}
