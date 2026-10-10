package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.common.exception.DuplicateUserStatusException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.common.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.user.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
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
class BasicUserStatusServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private UserStatusMapper userStatusMapper;

    @InjectMocks
    private BasicUserStatusService userStatusService;

    private final UUID userId = UUID.randomUUID();

    private User createUser() {
        User user = User.create("kim", "kim@test.com", "password1234");
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }

    private UserStatus createUserStatus(UUID id) {
        UserStatus userStatus = UserStatus.create(createUser());
        ReflectionTestUtils.setField(userStatus, "id", id);
        return userStatus;
    }

    @Nested
    @DisplayName("create")
    class Create {

        private final UserStatusCreateRequestDto request = new UserStatusCreateRequestDto(userId, null);

        @Test
        @DisplayName("성공 - 사용자 상태를 저장하고 반환한다")
        void create_success() {
            UserStatusDto expected = new UserStatusDto(UUID.randomUUID(), userId, Instant.now());
            given(userRepository.findById(userId)).willReturn(Optional.of(createUser()));
            given(userStatusRepository.findByUserId(userId)).willReturn(Optional.empty());
            given(userStatusMapper.toDto(any(UserStatus.class))).willReturn(expected);

            assertThat(userStatusService.create(request)).isEqualTo(expected);
            then(userStatusRepository).should().save(any(UserStatus.class));
        }

        @Test
        @DisplayName("실패 - 없는 사용자면 UserNotFoundException")
        void create_userNotFound_fail() {
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userStatusService.create(request))
                    .isInstanceOf(UserNotFoundException.class);
            then(userStatusRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 이미 사용자 상태가 있으면 DuplicateUserStatusException")
        void create_duplicate_fail() {
            given(userRepository.findById(userId)).willReturn(Optional.of(createUser()));
            given(userStatusRepository.findByUserId(userId)).willReturn(Optional.of(createUserStatus(UUID.randomUUID())));

            assertThatThrownBy(() -> userStatusService.create(request))
                    .isInstanceOf(DuplicateUserStatusException.class);
            then(userStatusRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("find / findAll")
    class Find {

        @Test
        @DisplayName("성공 - id로 사용자 상태를 조회한다")
        void find_success() {
            UUID id = UUID.randomUUID();
            UserStatus userStatus = createUserStatus(id);
            UserStatusDto expected = new UserStatusDto(id, userId, userStatus.getLastAccessAt());
            given(userStatusRepository.findById(id)).willReturn(Optional.of(userStatus));
            given(userStatusMapper.toDto(userStatus)).willReturn(expected);

            assertThat(userStatusService.find(id)).isEqualTo(expected);
        }

        @Test
        @DisplayName("실패 - 없는 id면 UserStatusNotFoundException")
        void find_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(userStatusRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userStatusService.find(id))
                    .isInstanceOf(UserStatusNotFoundException.class);
        }

        @Test
        @DisplayName("성공 - 전체 사용자 상태 목록을 반환한다")
        void findAll_success() {
            given(userStatusRepository.findAll())
                    .willReturn(List.of(createUserStatus(UUID.randomUUID()), createUserStatus(UUID.randomUUID())));
            given(userStatusMapper.toDto(any(UserStatus.class)))
                    .willReturn(new UserStatusDto(UUID.randomUUID(), userId, Instant.now()));

            assertThat(userStatusService.findAll()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("updateByUserId")
    class UpdateByUserId {

        @Test
        @DisplayName("성공 - 보낸 시간으로 마지막 접속 시간을 갱신한다")
        void update_success() {
            UserStatus userStatus = createUserStatus(UUID.randomUUID());
            Instant newTime = Instant.parse("2026-10-07T12:00:00Z");
            given(userStatusRepository.findByUserId(userId)).willReturn(Optional.of(userStatus));
            given(userStatusMapper.toDto(userStatus))
                    .willReturn(new UserStatusDto(userStatus.getId(), userId, newTime));

            UserStatusDto result = userStatusService.updateByUserId(userId, new UserStatusUpdateRequestDto(newTime));

            assertThat(userStatus.getLastAccessAt()).isEqualTo(newTime);
            assertThat(result.lastActiveAt()).isEqualTo(newTime);
        }

        @Test
        @DisplayName("실패 - 사용자 상태가 없으면 UserStatusNotFoundException")
        void update_notFound_fail() {
            given(userStatusRepository.findByUserId(userId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userStatusService.updateByUserId(userId, new UserStatusUpdateRequestDto(Instant.now())))
                    .isInstanceOf(UserStatusNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("성공 - 사용자 상태를 삭제한다")
        void delete_success() {
            UUID id = UUID.randomUUID();
            given(userStatusRepository.findById(id)).willReturn(Optional.of(createUserStatus(id)));

            userStatusService.delete(id);

            then(userStatusRepository).should().deleteById(id);
        }

        @Test
        @DisplayName("실패 - 없는 id면 UserStatusNotFoundException, 삭제하지 않는다")
        void delete_notFound_fail() {
            UUID id = UUID.randomUUID();
            given(userStatusRepository.findById(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userStatusService.delete(id))
                    .isInstanceOf(UserStatusNotFoundException.class);
            then(userStatusRepository).should(never()).deleteById(any());
        }
    }
}
