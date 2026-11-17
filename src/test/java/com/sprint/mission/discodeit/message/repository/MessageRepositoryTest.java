package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.RepositoryTestConfig;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TestEntityManager em;

    // 컨트롤러 기본값과 같은 정렬: 최신 메시지 먼저
    private final Pageable firstTwo = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    private User author;
    private Channel channel;

    private static final Instant T1 = Instant.parse("2026-10-06T09:00:00Z");
    private static final Instant T2 = Instant.parse("2026-10-06T10:00:00Z");
    private static final Instant T3 = Instant.parse("2026-10-06T11:00:00Z");

    @BeforeEach
    void setUp() {
        author = User.create("kim", "kim@test.com", "password1234");
        author.updateUserStatus(UserStatus.create(author));
        em.persist(author);
        channel = em.persist(new Channel(ChannelType.PUBLIC, "일반", null));
    }

    /**
     * createdAt은 Auditing이 저장 시점에 채우고 updatable=false라서,
     * 정렬/커서 테스트를 위해 저장 후 DB 값을 직접 고정한다.
     */
    private Message saveMessage(Channel channel, String content, Instant createdAt) {
        Message message = em.persist(Message.create(null, content, channel, author));
        em.flush();
        em.getEntityManager()
                .createNativeQuery("update messages set created_at = ? where id = ?")
                .setParameter(1, createdAt)
                .setParameter(2, message.getId())
                .executeUpdate();
        return message;
    }

    @Nested
    @DisplayName("findAllByChannelId (페이징 + 정렬)")
    class FindAllByChannelId {

        @Test
        @DisplayName("성공 - 최신순으로 페이지 크기만큼 조회하고 다음 페이지 여부를 알려준다")
        void findAllByChannelId_sortedAndPaged() {
            // given
            saveMessage(channel, "첫번째", T1);
            saveMessage(channel, "두번째", T2);
            saveMessage(channel, "세번째", T3);
            em.clear();

            // when
            Slice<Message> result = messageRepository.findAllByChannelId(channel.getId(), firstTwo);

            // then
            assertThat(result.getContent()).extracting(Message::getContent)
                    .containsExactly("세번째", "두번째");
            assertThat(result.hasNext()).isTrue();
            // @EntityGraph로 작성자를 함께 조회했는지
            assertThat(Hibernate.isInitialized(result.getContent().get(0).getAuthor())).isTrue();
        }

        @Test
        @DisplayName("실패 - 다른 채널의 메시지는 조회되지 않는다")
        void findAllByChannelId_otherChannelExcluded() {
            // given
            Channel otherChannel = em.persist(new Channel(ChannelType.PUBLIC, "다른방", null));
            saveMessage(otherChannel, "다른 채널 메시지", T1);
            em.clear();

            // when
            Slice<Message> result = messageRepository.findAllByChannelId(channel.getId(), firstTwo);

            // then
            assertThat(result.getContent()).isEmpty();
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("실패(경계) - 존재하지 않는 채널이면 빈 결과")
        void findAllByChannelId_unknownChannel() {
            saveMessage(channel, "첫번째", T1);
            em.clear();

            Slice<Message> result = messageRepository.findAllByChannelId(UUID.randomUUID(), firstTwo);

            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByChannelIdAndCreatedAtLessThan (커서 페이징)")
    class FindAllByCursor {

        @Test
        @DisplayName("성공 - 커서보다 이전에 작성된 메시지만 최신순으로 조회한다")
        void findByCursor_onlyOlderMessages() {
            // given
            saveMessage(channel, "첫번째", T1);
            saveMessage(channel, "두번째", T2);
            saveMessage(channel, "세번째", T3);
            em.clear();

            // when
            Slice<Message> result = messageRepository
                    .findAllByChannelIdAndCreatedAtLessThan(channel.getId(), T3, firstTwo);

            // then
            assertThat(result.getContent()).extracting(Message::getContent)
                    .containsExactly("두번째", "첫번째");
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("실패 - 커서보다 이전 메시지가 없으면 빈 결과")
        void findByCursor_noOlderMessages() {
            // given
            saveMessage(channel, "첫번째", T1);
            em.clear();

            // when
            Slice<Message> result = messageRepository
                    .findAllByChannelIdAndCreatedAtLessThan(channel.getId(), T1, firstTwo);

            // then
            assertThat(result.getContent()).isEmpty();
            assertThat(result.hasNext()).isFalse();
        }
    }
}
