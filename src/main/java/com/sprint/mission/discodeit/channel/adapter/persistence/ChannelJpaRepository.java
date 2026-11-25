package com.sprint.mission.discodeit.channel.adapter.persistence;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelJpaRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAllByType(ChannelType type);
}
