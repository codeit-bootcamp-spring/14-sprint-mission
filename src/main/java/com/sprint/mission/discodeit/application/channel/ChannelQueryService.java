package com.sprint.mission.discodeit.application.channel;


import com.sprint.mission.discodeit.application.channel.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.application.channel.required.ChannelQRepository;
import com.sprint.mission.discodeit.application.channel.required.ChannelRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.Channel;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelQueryService implements ChannelEntityFinder {

  private final ChannelRepository channelRepository;
  private final ChannelQRepository channelQRepository;

  @Override
  public Channel getEntityById(UUID channelId) {
    return channelRepository.findById(channelId).orElseThrow(() -> new DiscodeitRuntimeException(
        ExceptionType.CHANNEL_NOT_FOUND));
  }

  @Override
  public List<Channel> getAllByUserId(UUID userId) {
    return channelQRepository.findAllByUserId(userId);
  }

}
