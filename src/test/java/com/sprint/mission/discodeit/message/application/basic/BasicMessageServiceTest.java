package com.sprint.mission.discodeit.message.application.basic;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.common.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.common.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private MessageMapper messageMapper;
    // 단순 변환 로직이라 실제 객체를 사용
    @Spy
    private PageResponseMapper pageResponseMapper = new PageResponseMapper();

    @InjectMocks
    private BasicMessageService messageService;

    private Channel createChannel(UUID id) {
        Channel channel = new Channel(ChannelType.PUBLIC, "일반", null);
        ReflectionTestUtils.setField(channel, "id", id);
        return channel;
    }

    private User createUser(UUID id) {
        User user = User.create("kim", "kim@test.com", "password1234");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Message createMessage(UUID id, String content) {
        Message message = Message.create(null, content, createChannel(UUID.randomUUID()), createUser(UUID.randomUUID()));
        ReflectionTestUtils.setField(message, "id", id);
        return message;
    }

    private MessageDto messageDto(UUID id, String content, Instant createdAt) {
        return new MessageDto(id, createdAt, null, content, null, null, List.of());
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("성공 - 첨부파일 없이 메시지를 생성한다")
        void createMessage_success() {
            // given
            UUID channelId = UUID.randomUUID();
            UUID authorId = UUID.randomUUID();
            MessageCreateRequestDto request = new MessageCreateRequestDto(null, "안녕하세요", channelId, authorId);
            MessageDto expected = messageDto(UUID.randomUUID(), "안녕하세요", Instant.now());

            given(channelRepository.findById(channelId)).willReturn(Optional.of(createChannel(channelId)));
            given(userRepository.findById(authorId)).willReturn(Optional.of(createUser(authorId)));
            given(messageMapper.toDto(any(Message.class))).willReturn(expected);

            // when
            MessageDto result = messageService.create(request, null);

            // then
            assertThat(result).isEqualTo(expected);
            then(messageRepository).should().save(any(Message.class));
            then(binaryContentStorage).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("성공 - 첨부파일마다 BinaryContent를 저장하고 파일을 저장한다")
        void createMessage_withAttachments_success() {
            // given
            UUID channelId = UUID.randomUUID();
            UUID authorId = UUID.randomUUID();
            MessageCreateRequestDto request = new MessageCreateRequestDto(null, "파일 보냅니다", channelId, authorId);
            List<MultipartFile> attachments = List.of(
                    new MockMultipartFile("attachments", "a.txt", "text/plain", "a".getBytes()),
                    new MockMultipartFile("attachments", "b.txt", "text/plain", "b".getBytes()));

            given(channelRepository.findById(channelId)).willReturn(Optional.of(createChannel(channelId)));
            given(userRepository.findById(authorId)).willReturn(Optional.of(createUser(authorId)));
            given(messageMapper.toDto(any(Message.class)))
                    .willReturn(messageDto(UUID.randomUUID(), "파일 보냅니다", Instant.now()));

            // when
            messageService.create(request, attachments);

            // then
            then(binaryContentRepository).should(times(2)).save(any(BinaryContent.class));
            then(binaryContentStorage).should(times(2)).put(any(), any());
            then(messageRepository).should().save(any(Message.class));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 채널이면 ChannelNotFoundException")
        void createMessage_channelNotFound_fail() {
            // given
            UUID channelId = UUID.randomUUID();
            MessageCreateRequestDto request = new MessageCreateRequestDto(null, "안녕하세요", channelId, UUID.randomUUID());
            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.create(request, null))
                    .isInstanceOf(ChannelNotFoundException.class);
            then(messageRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 작성자면 UserNotFoundException")
        void createMessage_authorNotFound_fail() {
            // given
            UUID channelId = UUID.randomUUID();
            UUID authorId = UUID.randomUUID();
            MessageCreateRequestDto request = new MessageCreateRequestDto(null, "안녕하세요", channelId, authorId);
            given(channelRepository.findById(channelId)).willReturn(Optional.of(createChannel(channelId)));
            given(userRepository.findById(authorId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.create(request, null))
                    .isInstanceOf(UserNotFoundException.class);
            then(messageRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("성공 - 메시지 내용을 수정한다")
        void updateMessage_success() {
            // given
            UUID messageId = UUID.randomUUID();
            Message message = createMessage(messageId, "기존 내용");
            MessageDto expected = messageDto(messageId, "수정된 내용", Instant.now());

            given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
            given(messageMapper.toDto(message)).willReturn(expected);

            // when
            MessageDto result = messageService.update(messageId, new MessageUpdateRequestDto("수정된 내용"));

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(message.getContent()).isEqualTo("수정된 내용");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 메시지면 MessageNotFoundException")
        void updateMessage_notFound_fail() {
            // given
            UUID messageId = UUID.randomUUID();
            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.update(messageId, new MessageUpdateRequestDto("수정된 내용")))
                    .isInstanceOf(MessageNotFoundException.class);
            then(messageMapper).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("성공 - 메시지를 삭제한다")
        void deleteMessage_success() {
            // given
            UUID messageId = UUID.randomUUID();
            given(messageRepository.findById(messageId)).willReturn(Optional.of(createMessage(messageId, "내용")));

            // when
            messageService.delete(messageId);

            // then
            then(messageRepository).should().deleteById(messageId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 메시지면 MessageNotFoundException, 삭제하지 않는다")
        void deleteMessage_notFound_fail() {
            // given
            UUID messageId = UUID.randomUUID();
            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.delete(messageId))
                    .isInstanceOf(MessageNotFoundException.class);
            then(messageRepository).should(never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("findAllByChannelId")
    class FindAllByChannelId {

        @Test
        @DisplayName("성공 - 커서 없이 첫 페이지를 조회하고, 마지막 메시지 시간이 다음 커서가 된다")
        void findAllByChannelId_firstPage_success() {
            // given
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 2);
            Message newer = createMessage(UUID.randomUUID(), "최근");
            Message older = createMessage(UUID.randomUUID(), "이전");
            Instant newerAt = Instant.parse("2026-10-06T10:00:00Z");
            Instant olderAt = Instant.parse("2026-10-06T09:00:00Z");

            given(messageRepository.findAllByChannelId(channelId, pageable))
                    .willReturn(new SliceImpl<>(List.of(newer, older), pageable, true));
            given(messageMapper.toDto(newer)).willReturn(messageDto(newer.getId(), "최근", newerAt));
            given(messageMapper.toDto(older)).willReturn(messageDto(older.getId(), "이전", olderAt));

            // when
            PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

            // then
            assertThat(result.content()).hasSize(2);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.nextCursor()).isEqualTo(olderAt);
            then(messageRepository).should(never()).findAllByChannelIdAndCreatedAtLessThan(any(), any(), any());
        }

        @Test
        @DisplayName("성공 - 커서가 있으면 커서보다 이전 메시지를 조회한다")
        void findAllByChannelId_withCursor_success() {
            // given
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 2);
            Instant cursor = Instant.parse("2026-10-06T09:00:00Z");
            Message message = createMessage(UUID.randomUUID(), "더 이전");
            Instant messageAt = Instant.parse("2026-10-06T08:00:00Z");

            given(messageRepository.findAllByChannelIdAndCreatedAtLessThan(channelId, cursor, pageable))
                    .willReturn(new SliceImpl<>(List.of(message), pageable, false));
            given(messageMapper.toDto(message)).willReturn(messageDto(message.getId(), "더 이전", messageAt));

            // when
            PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor, pageable);

            // then
            assertThat(result.content()).hasSize(1);
            assertThat(result.hasNext()).isFalse();
            assertThat(result.nextCursor()).isEqualTo(messageAt);
        }

        @Test
        @DisplayName("실패(경계) - 메시지가 없으면 빈 목록과 null 커서를 반환한다")
        void findAllByChannelId_empty() {
            // given
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 2);
            given(messageRepository.findAllByChannelId(channelId, pageable))
                    .willReturn(new SliceImpl<>(List.of(), pageable, false));

            // when
            PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.hasNext()).isFalse();
            assertThat(result.nextCursor()).isNull();
        }
    }
}
