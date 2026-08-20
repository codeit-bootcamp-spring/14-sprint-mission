package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService  {

    ChannelResponseDto createPublic(PublicChannelCreateRequestDto request);
    ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request);
    ChannelResponseDto find(UUID id);
    ChannelResponseDto update(Integer id, ChannelUpdateRequestDto updateRequest);
    void delete(UUID id);
    List<Channel> findAll();


}
