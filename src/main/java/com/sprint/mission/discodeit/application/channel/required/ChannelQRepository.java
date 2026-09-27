package com.sprint.mission.discodeit.application.channel.required;

import com.sprint.mission.discodeit.domain.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelQRepository {

  List<Channel> findAllByUserId(UUID userId);

}
