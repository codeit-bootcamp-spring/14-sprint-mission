package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    private User createUser(UUID id, String username, String email) {
        User user = new User(username, email, "password", null);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Test
    @DisplayName("사용자 생성 성공")
    void create_success() {
        UserCreateRequest request = new UserCreateRequest("tester", "tester@test.com", "1234");
        UserDto expected = new UserDto(UUID.randomUUID(), "tester", "tester@test.com", null, true);
        given(userRepository.existsByUsername("tester")).willReturn(false);
        given(userRepository.existsByEmail("tester@test.com")).willReturn(false);
        given(userMapper.toDto(any(User.class))).willReturn(expected);

        UserDto result = userService.create(request, null);

        assertThat(result).isEqualTo(expected);
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 이미 있는 username")
    void create_fail_duplicateUsername() {
        UserCreateRequest request = new UserCreateRequest("tester", "tester@test.com", "1234");
        given(userRepository.existsByUsername("tester")).willReturn(true);

        assertThatThrownBy(() -> userService.create(request, null))
            .isInstanceOf(UserAlreadyExistsException.class);
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 수정 성공")
    void update_success() {
        UUID userId = UUID.randomUUID();
        User user = createUser(userId, "before", "before@test.com");
        UserUpdateRequest request = new UserUpdateRequest("after", null, null);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByUsername("after")).willReturn(false);

        userService.update(userId, request, null);

        assertThat(user.getUsername()).isEqualTo("after");
        assertThat(user.getEmail()).isEqualTo("before@test.com");
    }

    @Test
    @DisplayName("사용자 수정 실패 - 없는 사용자")
    void update_fail_userNotFound() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(
            () -> userService.update(userId, new UserUpdateRequest("after", null, null), null))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void delete_success() {
        UUID userId = UUID.randomUUID();
        User user = createUser(userId, "tester", "tester@test.com");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.delete(userId);

        then(userRepository).should().delete(user);
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 없는 사용자")
    void delete_fail_userNotFound() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userId))
            .isInstanceOf(UserNotFoundException.class);
        then(userRepository).should(never()).delete(any(User.class));
    }
}
