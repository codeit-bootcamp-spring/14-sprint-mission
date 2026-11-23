package com.sprint.mission.discodeit.channel.application.provided.command;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;

public interface ChannelModifier {

  ChannelDto modify(ModifyChannelCommand channelCommand);
}
