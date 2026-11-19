package com.sprint.mission.discodeit.core.application.channel.out;

import com.sprint.mission.discodeit.core.domain.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepositoryCustom {
    List<Channel> findAccessibleByUserId(UUID userId);
}
