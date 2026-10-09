package com.sprint.mission.discodeit.application.channel.out;

import com.sprint.mission.discodeit.domain.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChannelRepository extends ChannelRepositoryCustom, JpaRepository<Channel, UUID> {

}
