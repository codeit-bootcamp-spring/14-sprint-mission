package com.sprint.mission.discodeit.application.channel.provided.command;

import com.sprint.mission.discodeit.application.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.application.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.application.channel.dto.PublicChannelCreateRequest;

public interface ChannelRegister {

  ChannelDto registerPublic(PublicChannelCreateRequest request);

  ChannelDto registerPrivate(PrivateChannelCreateRequest request);
}
