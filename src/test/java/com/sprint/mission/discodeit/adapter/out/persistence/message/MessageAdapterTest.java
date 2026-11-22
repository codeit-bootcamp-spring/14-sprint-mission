package com.sprint.mission.discodeit.adapter.out.persistence.message;

import com.sprint.mission.discodeit.adapter.out.persistence.config.QueryDslConfig;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.message.Message;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
}, excludeAutoConfiguration = DataJpaRepositoriesAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = MessageJpaRepository.class)
@Import({QueryDslConfig.class, MessageAdapter.class})
class MessageAdapterTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageAdapter adapter;

    @Test
    void findsChannelMessagesAndLatestCreationTime() {
        User author = entityManager.persist(User.createUser("author", "author@example.com", "password", null));
        Channel channel = entityManager.persist(Channel.createPublicChannel("public", "description"));
        Message first = entityManager.persist(Message.of("first", author, channel, List.of()));
        Message second = entityManager.persist(Message.of("second", author, channel, List.of()));
        entityManager.flush();
        entityManager.clear();

        Instant expectedLatest = first.getCreatedAt().isAfter(second.getCreatedAt())
                ? first.getCreatedAt() : second.getCreatedAt();
        assertThat(adapter.findLastMessageAtByChannelId(List.of(channel.getId())))
                .containsEntry(channel.getId(), expectedLatest);

        Slice<Message> found = adapter.findAllByChannelId(
                channel.getId(),
                Instant.now().plusSeconds(1),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        assertThat(found.getContent()).extracting(Message::getId)
                .containsExactlyInAnyOrder(first.getId(), second.getId());
    }
}
