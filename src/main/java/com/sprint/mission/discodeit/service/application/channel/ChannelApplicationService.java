package com.sprint.mission.discodeit.service.application.channel;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ChannelApplicationService {
    ChannelResponseDto createPublic(PublicChannelCreateRequestDto publicChannelCreateRequest);
    ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto privateChannelCreateRequest);
    ChannelResponseDto findById(UUID channelId);
    List<ChannelResponseDto> findAllByUserId(UUID userId);
    ChannelResponseDto update(UUID channelId, ChannelUpdateRequestDto channelUpdateRequest);
    void delete(UUID channelId);
}
