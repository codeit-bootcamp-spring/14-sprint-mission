package com.sprint.mission.discodeit.repository.channel;

import com.sprint.mission.discodeit.domain.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepositoryCustom {
    List<Channel> findAccessibleByUserId(UUID userId);
}
