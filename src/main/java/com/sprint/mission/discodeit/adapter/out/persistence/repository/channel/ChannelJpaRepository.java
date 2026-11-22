package com.sprint.mission.discodeit.adapter.out.persistence.repository.channel;

import com.sprint.mission.discodeit.core.domain.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelJpaRepository extends JpaRepository<Channel, UUID>, ChannelQueryRepository {
}
