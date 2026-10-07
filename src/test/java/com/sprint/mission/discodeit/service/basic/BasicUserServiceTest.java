package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;

  @InjectMocks
  private BasicUserService userService;

  @Test
  @DisplayName("사용자 생성 성공: 사용자와 UserStatus를 함께 저장하고 DTO를 반환한다")
  void create_success() {
    // given
    UserCreateRequest request = new UserCreateRequest("alice", "alice@example.com", "pass1234");
    UserDto expected = new UserDto(UUID.randomUUID(), "alice", "alice@example.com", null, true);
    given(userRepository.existsByEmail("alice@example.com")).willReturn(false);
    given(userRepository.existsByUsername("alice")).willReturn(false);
    given(userMapper.toDto(any(User.class))).willReturn(expected);

    // when
    UserDto result = userService.create(request, Optional.empty());

    // then
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    then(userRepository).should().save(captor.capture());
    User saved = captor.getValue();
    assertThat(saved.getUsername()).isEqualTo("alice");
    assertThat(saved.getEmail()).isEqualTo("alice@example.com");
    assertThat(saved.getStatus()).isNotNull();
    assertThat(saved.getProfile()).isNull();
    then(binaryContentStorage).should(never()).put(any(), any());
    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("사용자 생성 성공: 프로필이 있으면 메타데이터와 파일 바이트를 저장한다")
  void create_withProfile() {
    // given
    UserCreateRequest request = new UserCreateRequest("alice", "alice@example.com", "pass1234");
    byte[] bytes = "image".getBytes();
    BinaryContentCreateRequest profile = new BinaryContentCreateRequest("profile.png",
        "image/png", bytes);
    given(userRepository.existsByEmail("alice@example.com")).willReturn(false);
    given(userRepository.existsByUsername("alice")).willReturn(false);

    // when
    userService.create(request, Optional.of(profile));

    // then
    ArgumentCaptor<BinaryContent> captor = ArgumentCaptor.forClass(BinaryContent.class);
    then(binaryContentRepository).should().save(captor.capture());
    assertThat(captor.getValue().getFileName()).isEqualTo("profile.png");
    assertThat(captor.getValue().getSize()).isEqualTo(bytes.length);
    then(binaryContentStorage).should().put(any(), eq(bytes));
  }

  @Test
  @DisplayName("사용자 생성 실패: 이미 존재하는 이메일이면 저장하지 않는다")
  void create_duplicateEmail() {
    // given
    UserCreateRequest request = new UserCreateRequest("alice", "taken@example.com", "pass1234");
    given(userRepository.existsByEmail("taken@example.com")).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class);
    then(userRepository).should(never()).save(any());
  }

  @Test
  @DisplayName("사용자 생성 실패: 이미 존재하는 username이면 저장하지 않는다")
  void create_duplicateUsername() {
    // given
    UserCreateRequest request = new UserCreateRequest("taken", "alice@example.com", "pass1234");
    given(userRepository.existsByEmail("alice@example.com")).willReturn(false);
    given(userRepository.existsByUsername("taken")).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class)
        .extracting("details")
        .isEqualTo(Map.of("username", "taken"));
    then(userRepository).should(never()).save(any());
  }

  @Test
  @DisplayName("사용자 수정 성공: 엔티티 값이 바뀌고 DTO를 반환한다")
  void update_success() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("old", "old@example.com", "password", null);
    UserUpdateRequest request = new UserUpdateRequest("new", "new@example.com", null);
    UserDto expected = new UserDto(userId, "new", "new@example.com", null, false);
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmailAndIdNot("new@example.com", userId)).willReturn(false);
    given(userRepository.existsByUsernameAndIdNot("new", userId)).willReturn(false);
    given(userMapper.toDto(user)).willReturn(expected);

    // when
    UserDto result = userService.update(userId, request, Optional.empty());

    // then
    assertThat(result).isEqualTo(expected);
    assertThat(user.getUsername()).isEqualTo("new");
    assertThat(user.getEmail()).isEqualTo("new@example.com");
  }

  @Test
  @DisplayName("사용자 수정 실패: 존재하지 않는 사용자")
  void update_userNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> userService.update(userId,
        new UserUpdateRequest("new", null, null), Optional.empty()))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("사용자 수정 실패: 다른 사용자가 쓰는 이메일")
  void update_duplicateEmail() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("old", "old@example.com", "password", null);
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmailAndIdNot("taken@example.com", userId)).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.update(userId,
        new UserUpdateRequest(null, "taken@example.com", null), Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class);
    assertThat(user.getEmail()).isEqualTo("old@example.com");
  }

  @Test
  @DisplayName("사용자 삭제 성공")
  void delete_success() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(true);

    // when
    userService.delete(userId);

    // then
    then(userRepository).should().deleteById(userId);
  }

  @Test
  @DisplayName("사용자 삭제 실패: 존재하지 않는 사용자면 삭제하지 않는다")
  void delete_userNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> userService.delete(userId))
        .isInstanceOf(UserNotFoundException.class);
    then(userRepository).should(never()).deleteById(any());
  }
}
