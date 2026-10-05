package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelUnitTest {
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelMapper channelMapper;

    private BasicChannelService channelService;

    @BeforeEach
    void setUp() {
        this.channelService = new BasicChannelService(
            channelRepository,
            readStatusRepository,
            messageRepository,
            userRepository,
            new ChannelMapper()
        ) ;
    }

    @Test
    @DisplayName("정상적인 입력을 받은 채널은 생성이 된다")
    void success_newChannel_CorrectRequest() {
        //given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
            "테스트채널",
            "테스트 채널 생성입니다."
        );
        given(channelRepository.save(any(Channel.class)))
            .willAnswer(returnsFirstArg());

        //when
        ChannelDto channelDto = channelService.create(request);

        //then
        assertEquals("테스트채널", channelDto.name());
        assertEquals(ChannelType.PUBLIC, channelDto.type());
    }

    @Test
    @DisplayName("정상적인 업데이트요청을 받은 채널은 업데이트가 된다")
    void success_updateChannel_CorrectRequest() {
        //given
        UUID uuid = UUID.randomUUID();
        Channel channel = new Channel(
            ChannelType.PUBLIC,
            "테스트채널",
            "테스트 채널 생성입니다."
        );

        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
            "업데이트 된 테스트채널",
            "테스트 채널을 업데이트 했습니다."
        );
        given(channelRepository.findById(uuid))
            .willReturn(Optional.of(channel));

        //when
        ChannelDto newChannelDto = channelService.update(uuid, request);

        //then
        assertEquals("업데이트 된 테스트채널", newChannelDto.name());
    }

    @Test
    @DisplayName("존재하지 않는 채널아이디로 업데이트요청을 받으면 예외를 반환한다.")
    void fail_updateChannel_NotFoundChannel() {
        //given
        UUID uuid = UUID.randomUUID();

        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
            "업데이트 된 테스트채널",
            "테스트 채널을 업데이트 했습니다."
        );

        given(channelRepository.findById(uuid))
            .willReturn(Optional.empty());

        //when,then
        assertThrows(
            ChannelNotFoundException.class,
            () -> channelService.update(uuid, request));
    }

    @Test
    @DisplayName("정상적인 채널삭제 요청을 받으면 채널을 삭제한다.")
    void success_deleteChannel_CorrectRequest() {
        //given
        Channel channel = new Channel(
            ChannelType.PUBLIC,
            "테스트채널",
            "테스트 채널 생성입니다."
        );

        given(channelRepository.findById(channel.getId()))
            .willReturn(Optional.of(channel));

        //when
        channelService.delete(channel.getId());

        //then
        verify(channelRepository).deleteById(channel.getId());
    }
    @Test
    @DisplayName("존재하지않는 채널삭제 요청을 받으면 예외를 반환한다.")
    void fail_deleteChannel_NotFoundChannel() {
        //given
        UUID uuid = UUID.randomUUID();

        given(channelRepository.findById(uuid))
            .willReturn(Optional.empty());

        //when,then
        assertThrows(ChannelNotFoundException.class,
            () -> channelService.delete(uuid));
    }
}
