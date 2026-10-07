package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
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
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  // default 메서드뿐인 인터페이스라 mock 대신 실제 동작을 쓰는 spy로 둔다
  @Spy
  private PageResponseMapper pageResponseMapper = new PageResponseMapper() {
  };

  @InjectMocks
  private BasicMessageService messageService;

  private final Channel channel = new Channel(ChannelType.PUBLIC, "general", null);
  private final User author = new User("author", "author@example.com", "pw", null);

  @Test
  @DisplayName("메시지 생성 성공: 첨부 파일 메타데이터와 바이트를 저장한다")
  void create_success() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("hello", channelId, authorId);
    BinaryContentCreateRequest attachment = new BinaryContentCreateRequest("a.txt", "text/plain",
        "abc".getBytes());
    MessageDto expected = new MessageDto(UUID.randomUUID(), Instant.now(), null, "hello",
        channelId, null, List.of());
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(author));
    given(messageMapper.toDto(any(Message.class))).willReturn(expected);

    // when
    MessageDto result = messageService.create(request, List.of(attachment));

    // then
    assertThat(result).isEqualTo(expected);
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should().put(any(), eq("abc".getBytes()));
    then(messageRepository).should().save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 실패: 존재하지 않는 채널이면 저장하지 않는다")
  void create_channelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> messageService.create(
        new MessageCreateRequest("hello", channelId, UUID.randomUUID()), List.of()))
        .isInstanceOf(ChannelNotFoundException.class);
    then(messageRepository).should(never()).save(any());
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void update_success() {
    // given
    UUID messageId = UUID.randomUUID();
    Message message = new Message("before", channel, author, List.of());
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

    // when
    messageService.update(messageId, new MessageUpdateRequest("after"));

    // then
    assertThat(message.getContent()).isEqualTo("after");
  }

  @Test
  @DisplayName("메시지 수정 실패: 존재하지 않는 메시지")
  void update_messageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> messageService.update(messageId, new MessageUpdateRequest("after")))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void delete_success() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(true);

    // when
    messageService.delete(messageId);

    // then
    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 실패: 존재하지 않는 메시지면 삭제하지 않는다")
  void delete_messageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> messageService.delete(messageId))
        .isInstanceOf(MessageNotFoundException.class);
    then(messageRepository).should(never()).deleteById(any());
  }

  @Test
  @DisplayName("채널 메시지 조회: 마지막 메시지의 createdAt이 다음 커서가 된다")
  void findAllByChannelId_success() {
    // given
    UUID channelId = UUID.randomUUID();
    PageRequest pageable = PageRequest.of(0, 2);
    Message m1 = new Message("1", channel, author, List.of());
    Message m2 = new Message("2", channel, author, List.of());
    Instant newer = Instant.parse("2026-10-07T10:00:00Z");
    Instant older = Instant.parse("2026-10-07T09:00:00Z");
    MessageDto dto1 = new MessageDto(UUID.randomUUID(), newer, null, "1", channelId, null, List.of());
    MessageDto dto2 = new MessageDto(UUID.randomUUID(), older, null, "2", channelId, null, List.of());
    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class),
        eq(pageable))).willReturn(new SliceImpl<>(List.of(m1, m2), pageable, true));
    given(messageMapper.toDto(m1)).willReturn(dto1);
    given(messageMapper.toDto(m2)).willReturn(dto2);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

    // then
    assertThat(result.content()).containsExactly(dto1, dto2);
    assertThat(result.nextCursor()).isEqualTo(older);
    assertThat(result.hasNext()).isTrue();
  }

  @Test
  @DisplayName("채널 메시지 조회: 메시지가 없으면 다음 커서는 null")
  void findAllByChannelId_empty() {
    // given
    UUID channelId = UUID.randomUUID();
    PageRequest pageable = PageRequest.of(0, 50);
    Instant cursor = Instant.parse("2026-10-07T09:00:00Z");
    given(messageRepository.findAllByChannelIdWithAuthor(channelId, cursor, pageable))
        .willReturn(new SliceImpl<>(List.of(), pageable, false));

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor,
        pageable);

    // then
    assertThat(result.content()).isEmpty();
    assertThat(result.nextCursor()).isNull();
    assertThat(result.hasNext()).isFalse();
  }
}
