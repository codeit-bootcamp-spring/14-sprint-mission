package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.application.channel.provided.command.ChannelCommand;
import com.sprint.mission.discodeit.application.channel.required.ChannelRepository;
import com.sprint.mission.discodeit.domain.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelCommandService implements ChannelCommand {

  private final ChannelRepository channelRepository;

  @Override
  public Channel createPublic(String name, String description) {
    Channel channel = Channel.createPublic(name, description);
    return channelRepository.save(channel);
  }

  @Override
  public Channel createPrivate() {
    Channel channel = Channel.createPrivate();
    return channelRepository.save(channel);
  }

  @Override
  public Channel update(Channel channel, String name, String description) {
    return channel.update(name, description);
  }

  @Override
  public void delete(Channel channel) {
    channelRepository.delete(channel);
  }
}
