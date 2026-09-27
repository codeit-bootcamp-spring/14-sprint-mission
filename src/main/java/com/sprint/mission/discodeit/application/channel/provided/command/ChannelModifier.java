package com.sprint.mission.discodeit.application.channel.provided.command;

import com.sprint.mission.discodeit.application.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.application.channel.dto.ChannelRequest;
import java.util.UUID;

public interface ChannelModifier {

  ChannelDto modify(UUID channelId, ChannelRequest request);
}
