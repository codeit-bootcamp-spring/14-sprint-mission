package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.common.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.common.exception.DuplicateUsernameException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    private User createUser(UUID id, String username, String email) {
        User user = User.create(username, email, "password1234");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("성공 - 프로필 없이 사용자를 생성한다")
        void createUser_success() {
            // given
            UserCreateRequestDto request = new UserCreateRequestDto("kim", "kim@test.com", "password1234");
            UserDto expected = new UserDto(UUID.randomUUID(), "kim", "kim@test.com", null, true);

            given(userRepository.findByUserName("kim")).willReturn(Optional.empty());
            given(userRepository.findByEmail("kim@test.com")).willReturn(Optional.empty());
            given(userMapper.toDto(any(User.class))).willReturn(expected);

            // when
            UserDto result = userService.create(request, null);

            // then
            assertThat(result).isEqualTo(expected);
            then(userRepository).should().save(any(User.class));
            then(binaryContentStorage).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("성공 - 프로필 이미지가 있으면 파일을 저장한다")
        void createUser_withProfile_success() {
            // given
            UserCreateRequestDto request = new UserCreateRequestDto("kim", "kim@test.com", "password1234");
            MockMultipartFile profile = new MockMultipartFile(
                    "profile", "profile.png", "image/png", new byte[]{1, 2, 3});

            given(userRepository.findByUserName("kim")).willReturn(Optional.empty());
            given(userRepository.findByEmail("kim@test.com")).willReturn(Optional.empty());
            given(userMapper.toDto(any(User.class)))
                    .willReturn(new UserDto(UUID.randomUUID(), "kim", "kim@test.com", null, true));

            // when
            userService.create(request, profile);

            // then
            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentStorage).should().put(any(), eq(new byte[]{1, 2, 3}));
            then(userRepository).should().save(any(User.class));
        }

        @Test
        @DisplayName("실패 - 이미 존재하는 username이면 DuplicateUsernameException")
        void createUser_duplicateUsername_fail() {
            // given
            UserCreateRequestDto request = new UserCreateRequestDto("kim", "kim@test.com", "password1234");
            given(userRepository.findByUserName("kim"))
                    .willReturn(Optional.of(createUser(UUID.randomUUID(), "kim", "other@test.com")));

            // when & then
            assertThatThrownBy(() -> userService.create(request, null))
                    .isInstanceOf(DuplicateUsernameException.class);
            then(userRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 - 이미 존재하는 email이면 DuplicateEmailException")
        void createUser_duplicateEmail_fail() {
            // given
            UserCreateRequestDto request = new UserCreateRequestDto("kim", "kim@test.com", "password1234");
            given(userRepository.findByUserName("kim")).willReturn(Optional.empty());
            given(userRepository.findByEmail("kim@test.com"))
                    .willReturn(Optional.of(createUser(UUID.randomUUID(), "lee", "kim@test.com")));

            // when & then
            assertThatThrownBy(() -> userService.create(request, null))
                    .isInstanceOf(DuplicateEmailException.class);
            then(userRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("성공 - 보낸 값만 수정된다")
        void updateUser_success() {
            // given
            UUID userId = UUID.randomUUID();
            User user = createUser(userId, "kim", "kim@test.com");
            UserUpdateRequestDto request = new UserUpdateRequestDto("newKim", null, null);
            UserDto expected = new UserDto(userId, "newKim", "kim@test.com", null, true);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userMapper.toDto(user)).willReturn(expected);

            // when
            UserDto result = userService.update(userId, request, null);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(user.getUserName()).isEqualTo("newKim");
            assertThat(user.getEmail()).isEqualTo("kim@test.com");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 사용자면 UserNotFoundException")
        void updateUser_notFound_fail() {
            // given
            UUID userId = UUID.randomUUID();
            UserUpdateRequestDto request = new UserUpdateRequestDto("newKim", null, null);
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.update(userId, request, null))
                    .isInstanceOf(UserNotFoundException.class);
            then(userMapper).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("성공 - 사용자를 삭제한다")
        void deleteUser_success() {
            // given
            UUID userId = UUID.randomUUID();
            given(userRepository.findById(userId))
                    .willReturn(Optional.of(createUser(userId, "kim", "kim@test.com")));

            // when
            userService.delete(userId);

            // then
            then(userRepository).should().deleteById(userId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 사용자면 UserNotFoundException, 삭제하지 않는다")
        void deleteUser_notFound_fail() {
            // given
            UUID userId = UUID.randomUUID();
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.delete(userId))
                    .isInstanceOf(UserNotFoundException.class);
            then(userRepository).should(never()).deleteById(any());
        }
    }
}
