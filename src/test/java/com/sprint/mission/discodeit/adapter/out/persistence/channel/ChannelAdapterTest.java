package com.sprint.mission.discodeit.adapter.out.persistence.channel;

import com.sprint.mission.discodeit.adapter.out.persistence.repository.channel.ChannelAdapter;
import com.sprint.mission.discodeit.adapter.out.persistence.repository.channel.ChannelJpaRepository;
import com.sprint.mission.discodeit.adapter.out.persistence.repository.config.QueryDslConfig;
import com.sprint.mission.discodeit.core.domain.channel.Channel;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
}, excludeAutoConfiguration = DataJpaRepositoriesAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = ChannelJpaRepository.class)
@Import({QueryDslConfig.class, ChannelAdapter.class})
class ChannelAdapterTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelAdapter adapter;

    @Test
    void accessibleChannelsIncludePublicAndParticipatingPrivateChannels() {
        User participant = entityManager.persist(User.createUser("member", "member@example.com", "password", null));
        User outsider = entityManager.persist(User.createUser("outsider", "outsider@example.com", "password", null));
        Channel publicChannel = entityManager.persist(Channel.createPublicChannel("public", "description"));
        Channel privateChannel = entityManager.persist(Channel.createPrivateChannel(List.of(participant.getId())));
        entityManager.persist(ReadStatus.of(participant, privateChannel, Instant.now()));
        entityManager.flush();
        entityManager.clear();

        assertThat(adapter.findAccessibleByUserId(participant.getId()))
                .extracting(Channel::getId)
                .containsExactlyInAnyOrder(publicChannel.getId(), privateChannel.getId());
        assertThat(adapter.findAccessibleByUserId(outsider.getId()))
                .extracting(Channel::getId)
                .containsExactly(publicChannel.getId());
    }
}
