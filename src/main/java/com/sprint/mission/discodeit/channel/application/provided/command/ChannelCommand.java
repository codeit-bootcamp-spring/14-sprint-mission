package com.sprint.mission.discodeit.channel.application.provided.command;

import com.sprint.mission.discodeit.channel.domain.Channel;

public interface ChannelCommand {

  Channel createPublic(String name, String description);

  Channel createPrivate();

  Channel update(Channel channel, String name, String description);

  void delete(Channel channel);
}
