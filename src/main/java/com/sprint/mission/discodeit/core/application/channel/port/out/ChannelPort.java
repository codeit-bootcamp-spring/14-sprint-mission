package com.sprint.mission.discodeit.core.application.channel.port.out;

import com.sprint.mission.discodeit.core.common.persistence.CrudRepository;
import com.sprint.mission.discodeit.core.domain.channel.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelPort extends CrudRepository<Channel, UUID> {
    List<Channel> findAccessibleByUserId(UUID userId);
}
