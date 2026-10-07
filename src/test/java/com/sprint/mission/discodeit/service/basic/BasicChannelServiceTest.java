package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

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

  @InjectMocks
  private BasicChannelService channelService;

  private static Channel channelWithId(ChannelType type, UUID id) {
    Channel channel = new Channel(type, type == ChannelType.PUBLIC ? "general" : null, null);
    ReflectionTestUtils.setField(channel, "id", id); // id는 JPA가 채우므로 테스트에서 직접 넣는다
    return channel;
  }

  @Test
  @DisplayName("공개 채널 생성 성공")
  void createPublic_success() {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("general", "desc");
    ChannelDto expected = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "general", "desc",
        List.of(), null);
    given(channelMapper.toDto(any(Channel.class))).willReturn(expected);

    // when
    ChannelDto result = channelService.create(request);

    // then
    ArgumentCaptor<Channel> captor = ArgumentCaptor.forClass(Channel.class);
    then(channelRepository).should().save(captor.capture());
    assertThat(captor.getValue().getType()).isEqualTo(ChannelType.PUBLIC);
    assertThat(captor.getValue().getName()).isEqualTo("general");
    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("비공개 채널 생성 성공: 참여자마다 ReadStatus를 만든다")
  void createPrivate_success() {
    // given
    UUID user1Id = UUID.randomUUID();
    UUID user2Id = UUID.randomUUID();
    List<User> users = List.of(
        new User("u1", "u1@example.com", "pw", null),
        new User("u2", "u2@example.com", "pw", null));
    given(userRepository.findAllById(List.of(user1Id, user2Id))).willReturn(users);

    // when
    channelService.create(new PrivateChannelCreateRequest(List.of(user1Id, user2Id)));

    // then
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ReadStatus>> captor = ArgumentCaptor.forClass(List.class);
    then(readStatusRepository).should().saveAll(captor.capture());
    assertThat(captor.getValue()).hasSize(2);
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 수정 성공: 공개 채널의 이름·설명이 바뀐다")
  void update_success() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = channelWithId(ChannelType.PUBLIC, channelId);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    // when
    channelService.update(channelId, new PublicChannelUpdateRequest("renamed", "new desc"));

    // then
    assertThat(channel.getName()).isEqualTo("renamed");
    assertThat(channel.getDescription()).isEqualTo("new desc");
  }

  @Test
  @DisplayName("채널 수정 실패: 비공개 채널은 수정할 수 없다")
  void update_privateChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId))
        .willReturn(Optional.of(channelWithId(ChannelType.PRIVATE, channelId)));

    // when & then
    assertThatThrownBy(() -> channelService.update(channelId,
        new PublicChannelUpdateRequest("renamed", null)))
        .isInstanceOf(PrivateChannelUpdateException.class);
  }

  @Test
  @DisplayName("채널 수정 실패: 존재하지 않는 채널")
  void update_channelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> channelService.update(channelId,
        new PublicChannelUpdateRequest("renamed", null)))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("채널 삭제 성공: 메시지와 읽음 상태도 함께 지운다")
  void delete_success() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);

    // when
    channelService.delete(channelId);

    // then
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패: 존재하지 않는 채널이면 아무것도 지우지 않는다")
  void delete_channelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(ChannelNotFoundException.class);
    then(messageRepository).should(never()).deleteAllByChannelId(any());
    then(channelRepository).should(never()).deleteById(any());
  }

  @Test
  @DisplayName("사용자별 채널 조회: 공개 채널 + 참여한 비공개 채널 ID로 조회한다")
  void findAllByUserId_success() {
    // given
    UUID userId = UUID.randomUUID();
    UUID privateId = UUID.randomUUID();
    Channel privateChannel = channelWithId(ChannelType.PRIVATE, privateId);
    ReadStatus readStatus = new ReadStatus(
        new User("u", "u@example.com", "pw", null), privateChannel, Instant.now());
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of(privateId)))
        .willReturn(List.of(privateChannel));
    ChannelDto dto = new ChannelDto(privateId, ChannelType.PRIVATE, null, null, List.of(), null);
    given(channelMapper.toDto(privateChannel)).willReturn(dto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).containsExactly(dto);
  }

  @Test
  @DisplayName("사용자별 채널 조회: 참여한 채널이 없으면 빈 ID 목록으로 조회한다")
  void findAllByUserId_noSubscription() {
    // given
    UUID userId = UUID.randomUUID();
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of()))
        .willReturn(List.of());

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).isEmpty();
    then(channelRepository).should().findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());
    then(channelMapper).should(never()).toDto(any());
  }
}
