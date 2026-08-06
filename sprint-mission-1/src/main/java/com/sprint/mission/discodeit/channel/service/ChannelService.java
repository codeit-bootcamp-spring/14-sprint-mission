package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponseDto channelCreate(ChannelPublicCreateRequestDto channelPublicCreateRequestDto);

    ChannelResponseDto privateChannelCreate(
        ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto);

    void channelUpdate(UUID channelId, ChannelUpdateRequestDto channelUpdateRequestDto);

    List<ChannelResponseDto> findAllByUserId(UUID userId);

    ChannelResponseDto findById(UUID channelId);

    void channelDelete(UUID channelId);
}
