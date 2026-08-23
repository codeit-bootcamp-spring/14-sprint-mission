package com.sprint.mission.discodeit.service.IService;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService  {

    ChannelResponseDto createPublic(PublicChannelCreateRequestDto request);
    ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request);
    ChannelResponseDto find(UUID id);
    ChannelResponseDto update(UUID id, ChannelUpdateRequestDto updateRequest);
    void delete(UUID id);
    List<Channel> findAll();


}
