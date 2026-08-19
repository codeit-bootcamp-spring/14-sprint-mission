package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.channel.Channel;

import java.util.UUID;

public interface ChannelRepository extends CrudRepository<Channel>{
    Channel updateName(UUID id, String name);
}
