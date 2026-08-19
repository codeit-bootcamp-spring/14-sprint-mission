package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends CrudRepository<Channel, UUID> {
    List<Channel> findAllChannelByIds(List<UUID> idList);
    List<Channel> findAllPublicChannel();
}
