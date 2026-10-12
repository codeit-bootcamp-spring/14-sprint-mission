package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("PUBLIC 채널 + id 목록에 있는 PRIVATE 채널만 조회된다")
    void findAllByTypeOrIdIn_success() {
        Channel publicChannel = channelRepository.save(
            new Channel(ChannelType.PUBLIC, "공개", null));
        Channel myPrivate = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));
        channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
            ChannelType.PUBLIC, List.of(myPrivate.getId()));

        assertThat(result).extracting(Channel::getId)
            .containsExactlyInAnyOrder(publicChannel.getId(), myPrivate.getId());
    }

    @Test
    @DisplayName("참여한 PRIVATE 채널이 없으면 남의 PRIVATE 채널은 조회되지 않는다")
    void findAllByTypeOrIdIn_noPrivate() {
        channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
            ChannelType.PUBLIC, List.of());

        assertThat(result).isEmpty();
    }
}
