package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.channel.Channel;

import java.util.UUID;

public interface ChannelRepository extends CrudRepository<Channel>{
    Channel updateNameAndDescription(UUID id, String name, String description);

    boolean existsById(UUID id);
}
