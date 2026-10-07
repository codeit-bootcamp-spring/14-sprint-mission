package com.sprint.mission.discodeit.channel.application.provided.command;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.application.dto.ChannelRequest;
import java.util.UUID;

public interface ChannelModifier {

  ChannelDto modify(UUID channelId, ChannelRequest request);
}
