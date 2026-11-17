package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelFindResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChannelResponseCustom {

    List<ChannelDto> findAllVisibleTo(UUID userId);
}
