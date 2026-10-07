package com.sprint.mission.discodeit.channel.application.required;

import com.sprint.mission.discodeit.channel.domain.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelQRepository {

  List<Channel> findAllByUserId(UUID userId);

}
