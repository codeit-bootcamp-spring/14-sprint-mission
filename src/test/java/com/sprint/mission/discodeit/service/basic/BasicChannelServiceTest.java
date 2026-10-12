package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
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

    private Channel createChannel(UUID id, ChannelType type, String name) {
        Channel channel = new Channel(type, name, null);
        ReflectionTestUtils.setField(channel, "id", id);
        return channel;
    }

    private ChannelDto toDto(Channel channel) {
        return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
            channel.getDescription(), List.of(), null);
    }

    @Test
    @DisplayName("PUBLIC 채널 생성 성공")
    void createPublic_success() {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", "공지 채널");
        ChannelDto expected = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지",
            "공지 채널", List.of(), null);
        given(channelMapper.toDto(any(Channel.class))).willReturn(expected);

        ChannelDto result = channelService.createPublic(request);

        assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
        assertThat(result.name()).isEqualTo("공지");
        then(channelRepository).should().save(any(Channel.class));
    }

    @Test
    @DisplayName("PUBLIC 채널 생성 실패 - 저장 중 DB 오류가 나면 예외가 그대로 전달된다")
    void createPublic_fail_saveError() {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", null);
        given(channelRepository.save(any(Channel.class)))
            .willThrow(new DataIntegrityViolationException("DB 오류"));

        assertThatThrownBy(() -> channelService.createPublic(request))
            .isInstanceOf(DataIntegrityViolationException.class);
        then(channelMapper).should(never()).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("PRIVATE 채널 생성 성공 - 참여자 수만큼 ReadStatus가 만들어진다")
    void createPrivate_success() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        Channel saved = createChannel(UUID.randomUUID(), ChannelType.PRIVATE, null);
        given(channelRepository.save(any(Channel.class))).willReturn(saved);
        given(userRepository.findById(userId1))
            .willReturn(Optional.of(new User("a", "a@test.com", "1234", null)));
        given(userRepository.findById(userId2))
            .willReturn(Optional.of(new User("b", "b@test.com", "1234", null)));
        given(channelMapper.toDto(saved)).willReturn(toDto(saved));

        ChannelDto result = channelService.createPrivate(
            new PrivateChannelCreateRequest(List.of(userId1, userId2)));

        assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
        then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));
    }

    @Test
    @DisplayName("PRIVATE 채널 생성 실패 - 없는 사용자가 참여자에 있으면 예외")
    void createPrivate_fail_userNotFound() {
        UUID unknownUserId = UUID.randomUUID();
        Channel saved = createChannel(UUID.randomUUID(), ChannelType.PRIVATE, null);
        given(channelRepository.save(any(Channel.class))).willReturn(saved);
        given(userRepository.findById(unknownUserId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> channelService.createPrivate(
            new PrivateChannelCreateRequest(List.of(unknownUserId))))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("채널 수정 성공")
    void update_success() {
        UUID channelId = UUID.randomUUID();
        Channel channel = createChannel(channelId, ChannelType.PUBLIC, "before");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        channelService.update(channelId, new PublicChannelUpdateRequest("after", "설명"));

        assertThat(channel.getName()).isEqualTo("after");
        assertThat(channel.getDescription()).isEqualTo("설명");
    }

    @Test
    @DisplayName("채널 수정 실패 - PRIVATE 채널은 수정할 수 없다")
    void update_fail_privateChannel() {
        UUID channelId = UUID.randomUUID();
        Channel channel = createChannel(channelId, ChannelType.PRIVATE, null);
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        assertThatThrownBy(() -> channelService.update(channelId,
            new PublicChannelUpdateRequest("after", null)))
            .isInstanceOf(PrivateChannelUpdateException.class);
    }

    @Test
    @DisplayName("채널 삭제 성공 - 채널의 메시지도 같이 지운다")
    void delete_success() {
        UUID channelId = UUID.randomUUID();
        Channel channel = createChannel(channelId, ChannelType.PUBLIC, "삭제할 채널");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(messageRepository.findAllByChannelId(channelId)).willReturn(List.of());

        channelService.delete(channelId);

        then(messageRepository).should().deleteAll(anyList());
        then(channelRepository).should().delete(channel);
    }

    @Test
    @DisplayName("채널 삭제 실패 - 없는 채널")
    void delete_fail_channelNotFound() {
        UUID channelId = UUID.randomUUID();
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> channelService.delete(channelId))
            .isInstanceOf(ChannelNotFoundException.class);
        then(channelRepository).should(never()).delete(any(Channel.class));
    }

    @Test
    @DisplayName("사용자별 채널 조회 성공 - PUBLIC 채널 + 내가 참여한 PRIVATE 채널")
    void findAllByUserId_success() {
        UUID userId = UUID.randomUUID();
        User user = new User("tester", "tester@test.com", "1234", null);
        Channel publicChannel = createChannel(UUID.randomUUID(), ChannelType.PUBLIC, "공개");
        Channel privateChannel = createChannel(UUID.randomUUID(), ChannelType.PRIVATE, null);
        given(readStatusRepository.findAllByUserId(userId))
            .willReturn(List.of(new ReadStatus(user, privateChannel, Instant.now())));
        given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
            List.of(privateChannel.getId())))
            .willReturn(List.of(publicChannel, privateChannel));
        given(channelMapper.toDto(publicChannel)).willReturn(toDto(publicChannel));
        given(channelMapper.toDto(privateChannel)).willReturn(toDto(privateChannel));

        List<ChannelDto> result = channelService.findAllByUserId(userId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ChannelDto::type)
            .containsExactly(ChannelType.PUBLIC, ChannelType.PRIVATE);
    }

    @Test
    @DisplayName("사용자별 채널 조회 - 채널이 하나도 없으면 빈 목록")
    void findAllByUserId_empty() {
        UUID userId = UUID.randomUUID();
        given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());
        given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList()))
            .willReturn(List.of());

        List<ChannelDto> result = channelService.findAllByUserId(userId);

        assertThat(result).isEmpty();
    }
}
