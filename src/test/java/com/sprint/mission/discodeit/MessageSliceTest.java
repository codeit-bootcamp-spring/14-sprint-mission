package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
public class MessageSliceTest {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    private Channel channel1;
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = userRepository.saveAndFlush(
            new User(
                "kimoo",
                "kim@gmail.com",
                "1234",
                null
            )
        );

        channel1 = channelRepository.saveAndFlush(
            new Channel(
                ChannelType.PUBLIC,
                "테스트 채널",
                "테스트채널압니다."
            )
        );

    }

    @Test
    @DisplayName("한개의 채널에서 모든 메세지를 조회합니다.")
    void success_findAllByChannelId() {
        //given
        messageRepository.saveAndFlush(
            new Message("안녕1", channel1, user1)
        );
        messageRepository.saveAndFlush(
            new Message("안녕2", channel1, user1)
        );
        messageRepository.saveAndFlush(
            new Message("안녕3", channel1, user1)
        );

        //when
        List<Message> result = messageRepository.findAllByChannelId(channel1.getId());

        //then
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("한개의 채널에서 모든 메세지를 삭제합니다.")
    void success_deleteAllByChannelId() {
        //given
        messageRepository.saveAndFlush(
            new Message("안녕1", channel1, user1)
        );
        messageRepository.saveAndFlush(
            new Message("안녕2", channel1, user1)
        );
        messageRepository.saveAndFlush(
            new Message("안녕3", channel1, user1)
        );

        //when
        messageRepository.deleteAllByChannelId(channel1.getId());

        //then
        List<Message> result =
            messageRepository.findAllByChannelId(channel1.getId());

        assertEquals(0, result.size());
    }
}
