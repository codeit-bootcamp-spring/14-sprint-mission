package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResponse;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelResponse channelCreate(ChannelPublicCreateRequestDto channelPublicCreateRequestDto);

    ChannelResponse privateChannelCreate(
        ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto);

    ChannelResponse channelUpdate(UUID channelId, ChannelUpdateRequestDto channelUpdateRequestDto);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto findById(UUID channelId);

    void channelDelete(UUID channelId);
}
