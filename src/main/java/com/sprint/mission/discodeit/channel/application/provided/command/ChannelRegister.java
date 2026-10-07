package com.sprint.mission.discodeit.channel.application.provided.command;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.application.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.application.dto.PublicChannelCreateRequest;

public interface ChannelRegister {

  ChannelDto registerPublic(PublicChannelCreateRequest request);

  ChannelDto registerPrivate(PrivateChannelCreateRequest request);
}
