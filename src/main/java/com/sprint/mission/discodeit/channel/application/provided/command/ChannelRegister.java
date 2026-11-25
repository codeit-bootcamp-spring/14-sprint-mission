package com.sprint.mission.discodeit.channel.application.provided.command;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;

public interface ChannelRegister {

//  ChannelDto registerPublic(PublicChannelCreateRequest request);

//  ChannelDto registerPrivate(PrivateChannelCreateRequest request);

  ChannelDto register(CreateChannel command);
}
