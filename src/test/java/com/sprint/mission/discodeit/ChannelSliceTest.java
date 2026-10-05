package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
public class ChannelSliceTest {
    @Autowired
    private ChannelRepository channelRepository;

    private Channel channel;

    @BeforeEach
    void setUp() {
        //given
        channel = new Channel(
            ChannelType.PUBLIC,
            "테스트 채널",
            "테스트 채널입니다"
        );
    }

    @Test
    @DisplayName("채널을 조회하고 채널 아이디로 조회합니다")
    void success_findById() {
        //given
        channelRepository.saveAndFlush(channel);

        //when
        Optional<Channel> result = channelRepository.findById(channel.getId());

        //then
        assertTrue(result.isPresent());
        assertEquals("테스트 채널", result.get().getName());
    }

    @Test
    @DisplayName("존재하지 않는 채널의 이름으로 채널 찾으면 실패한다")
    void fail_findById() {
        //given
        UUID uuid = UUID.randomUUID();

        //when
        Optional<Channel> result = channelRepository.findById(uuid);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("공개채널을 전부 조회한다.")
    void success_findAllByType() {
        //given
        Channel channel1 = new Channel(ChannelType.PUBLIC, "공개채널2", "공개채널 입니다.");
        Channel channel2 = new Channel(ChannelType.PUBLIC, "공개채널3", "공개채널 입니다.");
        Channel channel3 = new Channel(ChannelType.PRIVATE, "비공개채널", "비공개채널 입니다.");

        channelRepository.saveAllAndFlush(List.of(channel, channel1, channel2, channel3));
        //when
        List<Channel> result = channelRepository.findAllByType(ChannelType.PUBLIC);

        //then
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("채널을 이름기준으로 오름차순해서 조회한다")
    void success_findAllByPageAndSort() {
        //given
        Channel channel1 = new Channel(
            ChannelType.PUBLIC,
            "pamoo 채널",
            "테스트 채널입니다"
        );
        Channel channel2 = new Channel(
            ChannelType.PUBLIC,
            "leemoo 채널",
            "테스트 채널입니다"
        );
        Channel channel3 = new Channel(
            ChannelType.PUBLIC,
            "choimoo 채널",
            "테스트 채널입니다"
        );
        channelRepository.saveAllAndFlush(List.of(channel, channel1, channel2, channel3));

        PageRequest pageRequest = PageRequest.of(
            0,
            10,
            Sort.by(Sort.Direction.ASC, "name")
        );

        //when
        Page<Channel> result = channelRepository.findAll(pageRequest);

        //then
        assertEquals(4, result.getContent().size());
        assertEquals("choimoo 채널", result.getContent().get(0).getName());
        assertEquals("leemoo 채널", result.getContent().get(1).getName());
    }

}
