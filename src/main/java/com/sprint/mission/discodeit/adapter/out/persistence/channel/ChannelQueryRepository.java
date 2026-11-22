package com.sprint.mission.discodeit.adapter.out.persistence.channel;

import com.sprint.mission.discodeit.core.domain.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelQueryRepository {
    List<Channel> findAccessibleByUserId(UUID userId);
}
