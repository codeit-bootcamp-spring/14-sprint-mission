package com.sprint.mission.discodeit.channel.adapter.persistence;

import com.sprint.mission.discodeit.channel.application.required.ChannelRepository;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChannelRepositoryAdapter implements ChannelRepository {

  private final ChannelJpaRepository jpaRepository;
  private final ChannelQRepository qRepository;

  @Override
  public List<Channel> findAllByType(ChannelType type) {
    return jpaRepository.findAllByType(type);
  }

  @Override
  public List<Channel> findAllByUserId(UUID userId) {
    return qRepository.findAllByUserId(userId);
  }

  @Override
  public Optional<Channel> findById(UUID channelId) {
    return jpaRepository.findById(channelId);
  }

  @Override
  public Channel save(Channel channel) {
    return jpaRepository.save(channel);
  }

  @Override
  public void delete(Channel channel) {
    jpaRepository.delete(channel);
  }
}
