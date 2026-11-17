package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.common.RepositoryTestConfig;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private TestEntityManager em;

    private User saveUser(String username) {
        User user = User.create(username, username + "@test.com", "password1234");
        user.updateUserStatus(UserStatus.create(user));
        return em.persist(user);
    }

    private Channel saveChannel(ChannelType type, String name, User... participants) {
        Channel channel = em.persist(new Channel(type, name, null));
        for (User participant : participants) {
            em.persist(new ReadStatus(participant, channel));
        }
        return channel;
    }

    private Map<UUID, ChannelDto> byId(List<ChannelDto> channels) {
        return channels.stream().collect(Collectors.toMap(ChannelDto::id, Function.identity()));
    }

    @Test
    @DisplayName("성공 - PUBLIC 채널 전부와 내가 참여한 PRIVATE 채널만 조회된다")
    void findAllVisibleTo_publicAndMyPrivate() {
        // given
        User me = saveUser("me");
        User other = saveUser("other");
        Channel publicChannel = saveChannel(ChannelType.PUBLIC, "공지");
        Channel myPrivate = saveChannel(ChannelType.PRIVATE, null, me, other);
        Channel othersPrivate = saveChannel(ChannelType.PRIVATE, null, other);
        em.flush();
        em.clear();

        // when
        List<ChannelDto> result = channelRepository.findAllVisibleTo(me.getId());

        // then
        assertThat(result).extracting(ChannelDto::id)
                .containsExactlyInAnyOrder(publicChannel.getId(), myPrivate.getId())
                .doesNotContain(othersPrivate.getId());

        ChannelDto privateDto = byId(result).get(myPrivate.getId());
        assertThat(privateDto.participants()).extracting(UserDto::username)
                .containsExactlyInAnyOrder("me", "other");
    }

    @Test
    @DisplayName("성공 - 채널의 마지막 메시지 시간이 함께 조회된다")
    void findAllVisibleTo_lastMessageAt() {
        // given
        User me = saveUser("me");
        Channel withMessage = saveChannel(ChannelType.PUBLIC, "대화방");
        Channel empty = saveChannel(ChannelType.PUBLIC, "빈방");
        em.persist(Message.create(null, "첫 메시지", withMessage, me));
        em.persist(Message.create(null, "두번째 메시지", withMessage, me));
        em.flush();
        em.clear();

        // when
        Map<UUID, ChannelDto> result = byId(channelRepository.findAllVisibleTo(me.getId()));

        // then
        assertThat(result.get(withMessage.getId()).lastMessageAt())
                .isNotNull()
                .isCloseTo(Instant.now(), within(1, ChronoUnit.MINUTES));
        assertThat(result.get(empty.getId()).lastMessageAt()).isNull();
    }

    @Test
    @DisplayName("실패 - 어떤 PRIVATE 채널에도 참여하지 않았으면 PRIVATE 채널은 보이지 않는다")
    void findAllVisibleTo_noAccessToPrivate() {
        // given
        User me = saveUser("me");
        User other = saveUser("other");
        saveChannel(ChannelType.PRIVATE, null, other);
        em.flush();
        em.clear();

        // when
        List<ChannelDto> result = channelRepository.findAllVisibleTo(me.getId());

        // then
        assertThat(result).isEmpty();
    }
}
