package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private MessageMapper messageMapper;
    @Spy
    private PageResponseMapper pageResponseMapper = new PageResponseMapper();

    @InjectMocks
    private BasicMessageService messageService;

    private final UUID channelId = UUID.randomUUID();
    private final UUID authorId = UUID.randomUUID();

    private Message createMessage(UUID id, String content) {
        Channel channel = new Channel(ChannelType.PUBLIC, "채널", null);
        User author = new User("tester", "tester@test.com", "1234", null);
        Message message = new Message(content, channel, author, new ArrayList<>());
        ReflectionTestUtils.setField(message, "id", id);
        return message;
    }

    private MessageDto toDto(UUID id, String content) {
        return new MessageDto(id, Instant.now(), Instant.now(), content, channelId, null,
            List.of());
    }

    @Test
    @DisplayName("메시지 생성 성공 - 첨부파일도 저장된다")
    void create_success() {
        MessageCreateRequest request = new MessageCreateRequest("안녕", channelId, authorId);
        BinaryContentCreateRequest attachment = new BinaryContentCreateRequest(
            "a.txt", "text/plain", "hello".getBytes());
        given(userRepository.findById(authorId))
            .willReturn(Optional.of(new User("tester", "tester@test.com", "1234", null)));
        given(channelRepository.findById(channelId))
            .willReturn(Optional.of(new Channel(ChannelType.PUBLIC, "채널", null)));
        given(messageMapper.toDto(any(Message.class)))
            .willReturn(toDto(UUID.randomUUID(), "안녕"));

        MessageDto result = messageService.create(request, List.of(attachment));

        assertThat(result.content()).isEqualTo("안녕");
        then(binaryContentRepository).should().save(any(BinaryContent.class));
        then(binaryContentStorage).should().put(any(), any());
        then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 없는 작성자")
    void create_fail_authorNotFound() {
        MessageCreateRequest request = new MessageCreateRequest("안녕", channelId, authorId);
        given(userRepository.findById(authorId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.create(request, List.of()))
            .isInstanceOf(UserNotFoundException.class);
        then(messageRepository).should(never()).save(any(Message.class));
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void update_success() {
        UUID messageId = UUID.randomUUID();
        Message message = createMessage(messageId, "before");
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        messageService.update(messageId, new MessageUpdateRequest("after"));

        assertThat(message.getContent()).isEqualTo("after");
    }

    @Test
    @DisplayName("메시지 수정 실패 - 없는 메시지")
    void update_fail_messageNotFound() {
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.update(messageId,
            new MessageUpdateRequest("after")))
            .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void delete_success() {
        UUID messageId = UUID.randomUUID();
        Message message = createMessage(messageId, "삭제할 메시지");
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        messageService.delete(messageId);

        then(messageRepository).should().delete(message);
    }

    @Test
    @DisplayName("메시지 삭제 실패 - 없는 메시지")
    void delete_fail_messageNotFound() {
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.delete(messageId))
            .isInstanceOf(MessageNotFoundException.class);
        then(messageRepository).should(never()).delete(any(Message.class));
    }

    @Test
    @DisplayName("채널별 메시지 조회 성공 - 다음 페이지가 있으면 hasNext가 true")
    void findAllByChannelId_success() {
        Pageable pageable = PageRequest.of(0, 2);
        Message message1 = createMessage(UUID.randomUUID(), "첫번째");
        Message message2 = createMessage(UUID.randomUUID(), "두번째");
        given(messageRepository.findAllByChannelId(channelId, pageable))
            .willReturn(new SliceImpl<>(List.of(message1, message2), pageable, true));
        given(messageMapper.toDto(message1)).willReturn(toDto(message1.getId(), "첫번째"));
        given(messageMapper.toDto(message2)).willReturn(toDto(message2.getId(), "두번째"));

        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, pageable);

        assertThat(result.content()).extracting(MessageDto::content)
            .containsExactly("첫번째", "두번째");
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("채널별 메시지 조회 - 메시지가 없으면 빈 목록")
    void findAllByChannelId_empty() {
        Pageable pageable = PageRequest.of(0, 50);
        given(messageRepository.findAllByChannelId(channelId, pageable))
            .willReturn(new SliceImpl<>(List.of(), pageable, false));

        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, pageable);

        assertThat(result.content()).isEmpty();
        assertThat(result.hasNext()).isFalse();
    }
}
