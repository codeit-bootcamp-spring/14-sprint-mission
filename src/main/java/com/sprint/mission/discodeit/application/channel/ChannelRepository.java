package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.adaptor.persistence.ChannelQRepository;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID>, ChannelQRepository {

  List<Channel> findAllByType(ChannelType type);
}
