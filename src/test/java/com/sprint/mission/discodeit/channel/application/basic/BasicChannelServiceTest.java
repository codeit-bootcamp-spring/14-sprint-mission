package com.sprint.mission.discodeit.channel.application.basic;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

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
        Channel channel = new Channel(type, name, "설명");
        ReflectionTestUtils.setField(channel, "id", id);
        return channel;
    }

    private User createUser(UUID id, String username) {
        User user = User.create(username, username + "@test.com", "password1234");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Nested
    @DisplayName("create - PUBLIC")
    class CreatePublic {

        @Test
        @DisplayName("성공 - PUBLIC 채널을 저장하고 참여자 없이 반환한다")
        void createPublicChannel_success() {
            // given
            PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", "공지 채널");
            ChannelDto expected = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지", "공지 채널", List.of(), null);
            given(channelMapper.toDto(any(Channel.class), anyList(), isNull())).willReturn(expected);

            // when
            ChannelDto result = channelService.publicCreate(request);

            // then
            assertThat(result).isEqualTo(expected);

            ArgumentCaptor<Channel> captor = ArgumentCaptor.forClass(Channel.class);
            then(channelRepository).should().save(captor.capture());
            assertThat(captor.getValue().getChannelType()).isEqualTo(ChannelType.PUBLIC);
            assertThat(captor.getValue().getName()).isEqualTo("공지");
            then(readStatusRepository).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("create - PRIVATE")
    class CreatePrivate {

        @Test
        @DisplayName("성공 - 참여자마다 ReadStatus를 만들고 PRIVATE 채널을 저장한다")
        void createPrivateChannel_success() {
            // given
            UUID user1Id = UUID.randomUUID();
            UUID user2Id = UUID.randomUUID();
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(user1Id, user2Id));

            given(userRepository.findById(user1Id)).willReturn(Optional.of(createUser(user1Id, "kim")));
            given(userRepository.findById(user2Id)).willReturn(Optional.of(createUser(user2Id, "lee")));
            ChannelDto expected = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, null, null, List.of(), null);
            given(channelMapper.toDto(any(Channel.class), anyList(), isNull())).willReturn(expected);

            // when
            ChannelDto result = channelService.privateCreate(request);

            // then
            assertThat(result).isEqualTo(expected);
            then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));

            ArgumentCaptor<Channel> captor = ArgumentCaptor.forClass(Channel.class);
            then(channelRepository).should().save(captor.capture());
            assertThat(captor.getValue().getChannelType()).isEqualTo(ChannelType.PRIVATE);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 참여자가 있으면 UserNotFoundException, 읽음 상태를 만들지 않는다")
        void createPrivateChannel_userNotFound_fail() {
            // given
            UUID unknownUserId = UUID.randomUUID();
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(unknownUserId));
            given(userRepository.findById(unknownUserId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.privateCreate(request))
                    .isInstanceOf(UserNotFoundException.class);
            then(readStatusRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("성공 - PUBLIC 채널의 이름과 설명을 수정한다")
        void updateChannel_success() {
            // given
            UUID channelId = UUID.randomUUID();
            Channel channel = createChannel(channelId, ChannelType.PUBLIC, "기존이름");
            ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("새이름", "새설명");
            ChannelDto expected = new ChannelDto(channelId, ChannelType.PUBLIC, "새이름", "새설명", List.of(), null);

            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(messageRepository.findAllByChannelId(channelId)).willReturn(List.of());
            given(channelMapper.toDto(any(Channel.class), anyList(), isNull())).willReturn(expected);

            // when
            ChannelDto result = channelService.update(channelId, request);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(channel.getName()).isEqualTo("새이름");
            assertThat(channel.getDescription()).isEqualTo("새설명");
        }

        @Test
        @DisplayName("실패 - PRIVATE 채널은 수정할 수 없다")
        void updateChannel_private_fail() {
            // given
            UUID channelId = UUID.randomUUID();
            Channel channel = createChannel(channelId, ChannelType.PRIVATE, null);
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

            // when & then
            assertThatThrownBy(() -> channelService.update(channelId, new ChannelUpdateRequestDto("새이름", null)))
                    .isInstanceOf(PrivateChannelUpdateNotAllowedException.class);
            assertThat(channel.getName()).isNull();
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 채널이면 ChannelNotFoundException")
        void updateChannel_notFound_fail() {
            // given
            UUID channelId = UUID.randomUUID();
            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.update(channelId, new ChannelUpdateRequestDto("새이름", null)))
                    .isInstanceOf(ChannelNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("성공 - 채널을 삭제한다")
        void deleteChannel_success() {
            // given
            UUID channelId = UUID.randomUUID();

            // when
            channelService.delete(channelId);

            // then
            then(channelRepository).should().deleteById(channelId);
        }
    }

    @Nested
    @DisplayName("findAllByUserId")
    class FindAllByUserId {

        @Test
        @DisplayName("성공 - 사용자가 볼 수 있는 채널 목록을 반환한다")
        void findAllByUserId_success() {
            // given
            UUID userId = UUID.randomUUID();
            List<ChannelDto> channels = List.of(
                    new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지", null, List.of(), null),
                    new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, null, null, List.of(), null));
            given(channelRepository.findAllVisibleTo(userId)).willReturn(channels);

            // when
            List<ChannelDto> result = channelService.findAllByUserId(userId);

            // then
            assertThat(result).hasSize(2).isEqualTo(channels);
        }

        @Test
        @DisplayName("실패(경계) - 볼 수 있는 채널이 없으면 빈 목록을 반환한다")
        void findAllByUserId_empty() {
            // given
            UUID userId = UUID.randomUUID();
            given(channelRepository.findAllVisibleTo(userId)).willReturn(List.of());

            // when
            List<ChannelDto> result = channelService.findAllByUserId(userId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
