package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("공개 채널 전체와 내가 참여한 비공개 채널을 함께 조회한다")
  void findAllByTypeOrIdIn_success() {
    Channel publicChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "general", ""));
    Channel myPrivate = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));
    channelRepository.save(new Channel(ChannelType.PRIVATE, null, null)); // 남의 비공개 채널

    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(myPrivate.getId()));

    assertThat(channels)
        .extracting(Channel::getId)
        .containsExactlyInAnyOrder(publicChannel.getId(), myPrivate.getId());
  }

  @Test
  @DisplayName("참여한 비공개 채널이 없으면 공개 채널만 조회한다")
  void findAllByTypeOrIdIn_onlyPublic() {
    Channel publicChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "general", ""));
    channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));

    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    assertThat(channels)
        .extracting(Channel::getId)
        .containsExactly(publicChannel.getId());
  }
}
