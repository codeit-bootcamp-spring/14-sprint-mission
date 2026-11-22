package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    private Channel channel;
    private Channel emptyChannel;

    @BeforeEach
    void setUp() {
        User author = userRepository.save(new User("tester", "tester@test.com", "1234", null));
        channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "채널", null));
        emptyChannel = channelRepository.save(new Channel(ChannelType.PUBLIC, "빈 채널", null));
        for (int i = 1; i <= 3; i++) {
            messageRepository.save(new Message("메시지" + i, channel, author, new ArrayList<>()));
        }
    }

    @Test
    @DisplayName("페이징+정렬 조회 - 첫 페이지는 2개, 다음 페이지가 있다")
    void findAllByChannelId_firstPage() {
        PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Message> result = messageRepository.findAllByChannelId(channel.getId(), pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.getContent()).extracting(Message::getCreatedAt)
            .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("페이징 조회 - 메시지가 없는 채널이면 빈 결과")
    void findAllByChannelId_empty() {
        Slice<Message> result = messageRepository.findAllByChannelId(
            emptyChannel.getId(), PageRequest.of(0, 50));

        assertThat(result.getContent()).isEmpty();
        assertThat(result.hasNext()).isFalse();
    }
}
