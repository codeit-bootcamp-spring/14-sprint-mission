package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentsRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageUnitTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private MessageAttachmentsRepository messageAttachmentsRepository;

    private BasicMessageService messageService;

    @BeforeEach
    void setUp() {
        this.messageService = new BasicMessageService(
            messageRepository,
            channelRepository,
            userRepository,
            binaryContentRepository,
            messageAttachmentsRepository,
            new MessageMapper()
        );
    }

    @Test
    @DisplayName("정상적인 입력을 받은 메세지는 생성이 된다")
    void success_newMessage_CorrectRequest() {
        //given
        Channel channel = new Channel(
            ChannelType.PUBLIC,
            "테스트채널",
            "테스트 채널 생성입니다."
        );
        User user = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );
        MessageCreateRequest request = new MessageCreateRequest(
            "테스트 메세지입니다.",
            channel.getId(),
            user.getId()
        );

        given(channelRepository.existsById(channel.getId()))
            .willReturn(true);
        given(userRepository.existsById(user.getId()))
            .willReturn(true);
        given(channelRepository.getReferenceById(channel.getId()))
            .willReturn(channel);
        given(userRepository.getReferenceById(user.getId()))
            .willReturn(user);
        given(messageRepository.save(any(Message.class)))
            .willAnswer(returnsFirstArg());

        //when
        MessageDto messageDto = messageService.create(request, List.of());

        //then
        assertEquals("테스트 메세지입니다.", messageDto.content());
        assertEquals(channel.getId(), messageDto.channelId());
        assertEquals(user.getId(), messageDto.authorId());
    }

    @Test
    @DisplayName("존재하지 않는 채널에 메세지를 생성하면 예외를 반환한다")
    void fail_newMessage_NotFoundChannel() {
        //given
        UUID channelId = UUID.randomUUID();
        MessageCreateRequest request = new MessageCreateRequest(
            "테스트 메세지입니다.",
            channelId,
            UUID.randomUUID()
        );

        given(channelRepository.existsById(channelId))
            .willReturn(false);

        //when,then
        assertThrows(
            ChannelNotFoundException.class,
            () -> messageService.create(request, List.of())
        );
    }

    @Test
    @DisplayName("정상적인 업데이트요청을 받은 메세지는 업데이트가 된다")
    void success_updateMessage_CorrectRequest() {
        //given
        Channel channel = new Channel(
            ChannelType.PUBLIC,
            "테스트채널",
            "테스트 채널 생성입니다."
        );
        User user = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );
        Message message = new Message("수정 전 메세지입니다.", channel, user);
        MessageUpdateRequest request = new MessageUpdateRequest("수정 된 메세지입니다.");

        given(messageRepository.findById(message.getId()))
            .willReturn(Optional.of(message));
        given(messageAttachmentsRepository.findAllByMessageId(message.getId()))
            .willReturn(List.of());

        //when
        MessageDto messageDto = messageService.update(message.getId(), request);

        //then
        assertEquals("수정 된 메세지입니다.", messageDto.content());
    }

    @Test
    @DisplayName("존재하지 않는 메세지아이디로 업데이트요청을 받으면 예외를 반환한다")
    void fail_updateMessage_NotFoundMessage() {
        //given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("수정 된 메세지입니다.");

        given(messageRepository.findById(messageId))
            .willReturn(Optional.empty());

        //when,then
        assertThrows(
            MessageNotFoundException.class,
            () -> messageService.update(messageId, request)
        );
    }

    @Test
    @DisplayName("정상적인 메세지삭제 요청을 받으면 메세지를 삭제한다")
    void success_deleteMessage_CorrectRequest() {
        //given
        Channel channel = new Channel(
            ChannelType.PUBLIC,
            "테스트채널",
            "테스트 채널 생성입니다."
        );
        User user = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );
        Message message = new Message("테스트 메세지입니다.", channel, user);

        given(messageRepository.findById(message.getId()))
            .willReturn(Optional.of(message));
        given(messageAttachmentsRepository.findAllByMessageId(message.getId()))
            .willReturn(List.of());

        //when
        messageService.delete(message.getId());

        //then
        then(messageRepository).should().delete(message);
    }

    @Test
    @DisplayName("존재하지않는 메세지삭제 요청을 받으면 예외를 반환한다")
    void fail_deleteMessage_NotFoundMessage() {
        //given
        UUID messageId = UUID.randomUUID();

        given(messageRepository.findById(messageId))
            .willReturn(Optional.empty());

        //when,then
        assertThrows(
            MessageNotFoundException.class,
            () -> messageService.delete(messageId)
        );
    }
}
