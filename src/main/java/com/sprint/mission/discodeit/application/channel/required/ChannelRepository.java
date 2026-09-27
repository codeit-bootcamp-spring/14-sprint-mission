package com.sprint.mission.discodeit.application.channel.required;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAllByType(ChannelType type);
}
