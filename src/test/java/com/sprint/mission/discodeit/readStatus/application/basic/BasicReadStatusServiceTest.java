package com.sprint.mission.discodeit.readStatus.application.basic;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.DuplicateReadStatusException;
import com.sprint.mission.discodeit.common.exception.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readStatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

@ExtendWith(MockitoExtension.class)
class BasicReadStatusServiceTest {

    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ReadStatusMapper readStatusMapper;

    @InjectMocks
    private BasicReadStatusService readStatusService;

    private final UUID userId = UUID.randomUUID();
    private final UUID channelId = UUID.randomUUID();

    private User createUser() {
        User user = User.create("kim", "kim@test.com", "password1234");
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }

    private Channel createChannel() {
        Channel channel = new Channel(ChannelType.PUBLIC, "일반", null);
        ReflectionTestUtils.setField(channel, "id", channelId);
        return channel;
    }

    private ReadStatus createReadStatus(UUID id) {
        ReadStatus readStatus = ReadStatus.create(createUser(), createChannel(), Instant.parse("2026-10-01T00:00:00Z"));
        ReflectionTestUtils.setField(readStatus, "id", id);
        return readStatus;
    }

    @Nested
    @DisplayName("create")
    class Create {

        private final Instant lastReadAt = Instant.parse("2026-10-06T10:00:00Z");
        private final ReadStatusCreateRequestDto request = new ReadStatusCreateRequestDto(userId, channelId, lastReadAt);

        @Test
        @DisplayName("성공 - 읽음 상태를 저장하고 반환한다")
        void create_success() {
            // given
            ReadStatusDto expected = new ReadStatusDto(UUID.randomUUID(), userId, channelId, lastReadAt);
            given(channelRepository.findById(channelId)).willReturn(Optional.of(createChannel()));
            given(userRepository.findById(userId)).willReturn(Optional.of(createUser()));
            given(readStatusRepository.existsByChannelIdAndUserId(channelId, userId)).willReturn(false);
            given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(expected);

            // when
            ReadStatusDto result = readStatusService.create(request);

            // then
            assertThat(result).isEqualTo(expected);
            then(readStatusRepository).should().save(any(ReadStatus.class));
        }

        @Test
        @DisplayName("실패 - 없는 채널이면 ChannelNotFoundException")
        void create_channelNotFound_fail() {
            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readStatusService.create(request))
                    .isInstanceOf(ChannelNotFoundException.class);
            then(readStatusRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 없는 사용자면 UserNotFoundException")
        void create_userNotFound_fail() {
            given(channelRepository.findById(channelId)).willReturn(Optional.of(createChannel()));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readStatusService.create(request))
                    .isInstanceOf(UserNotFoundException.class);
            then(readStatusRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 이미 읽음 상태가 있으면 DuplicateReadStatusException")
        void create_duplicate_fail() {
            given(channelRepository.findById(channelId)).willReturn(Optional.of(createChannel()));
            given(userRepository.findById(userId)).willReturn(Optional.of(createUser()));
            given(readStatusRepository.existsByChannelIdAndUserId(channelId, userId)).willReturn(true);

            assertThatThrownBy(() -> readStatusService.create(request))
                    .isInstanceOf(DuplicateReadStatusException.class);
            then(readStatusRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("find / findAllByUserId")
    class Find {

        @Test
        @DisplayName("성공 - id로 읽음 상태를 조회한다")
        void find_success() {
            UUID id = UUID.randomUUID();
            ReadStatus readStatus = createReadStatus(id);
            ReadStatusDto expected = new ReadStatusDto(id, userId, channelId, readStatus.getLastReadTime());
            given(readStatusRepository.findById(id)).willReturn(Optional.of(readStatus));
            given(readStatusMapper.toDto(readStatus)).willReturn(expected);

            assertThat(readStatusService.find(id)).isEqualTo(expected);
        }

        @Test
        @DisplayName("실패 - 없는 id면 ReadStatusNotFoundException")
        void find_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(readStatusRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readStatusService.find(id))
                    .isInstanceOf(ReadStatusNotFoundException.class);
        }

        @Test
        @DisplayName("성공 - 사용자의 읽음 상태 목록을 반환한다")
        void findAllByUserId_success() {
            ReadStatus rs1 = createReadStatus(UUID.randomUUID());
            ReadStatus rs2 = createReadStatus(UUID.randomUUID());
            given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(rs1, rs2));
            given(readStatusMapper.toDto(any(ReadStatus.class)))
                    .willReturn(new ReadStatusDto(UUID.randomUUID(), userId, channelId, Instant.now()));

            assertThat(readStatusService.findAllByUserId(userId)).hasSize(2);
        }

        @Test
        @DisplayName("실패(경계) - 읽음 상태가 없으면 빈 목록")
        void findAllByUserId_empty() {
            given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());

            assertThat(readStatusService.findAllByUserId(userId)).isEmpty();
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("성공 - 마지막으로 읽은 시간을 갱신한다")
        void update_success() {
            UUID id = UUID.randomUUID();
            ReadStatus readStatus = createReadStatus(id);
            Instant newTime = Instant.parse("2026-10-07T12:00:00Z");
            given(readStatusRepository.findById(id)).willReturn(Optional.of(readStatus));
            given(readStatusMapper.toDto(readStatus))
                    .willReturn(new ReadStatusDto(id, userId, channelId, newTime));

            ReadStatusDto result = readStatusService.update(id, new ReadStatusUpdateRequestDto(newTime));

            assertThat(readStatus.getLastReadTime()).isEqualTo(newTime);
            assertThat(result.lastReadAt()).isEqualTo(newTime);
        }

        @Test
        @DisplayName("실패 - 없는 id면 ReadStatusNotFoundException")
        void update_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(readStatusRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readStatusService.update(id, new ReadStatusUpdateRequestDto(Instant.now())))
                    .isInstanceOf(ReadStatusNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("성공 - 읽음 상태를 삭제한다")
        void delete_success() {
            UUID id = UUID.randomUUID();
            given(readStatusRepository.findById(id)).willReturn(Optional.of(createReadStatus(id)));

            readStatusService.delete(id);

            then(readStatusRepository).should().deleteById(id);
        }

        @Test
        @DisplayName("실패 - 없는 id면 ReadStatusNotFoundException, 삭제하지 않는다")
        void delete_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(readStatusRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readStatusService.delete(id))
                    .isInstanceOf(ReadStatusNotFoundException.class);
            then(readStatusRepository).should(never()).deleteById(any());
        }
    }
}
