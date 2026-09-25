package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.adaptor.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.adaptor.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.application.user.required.UserRepository;
import com.sprint.mission.discodeit.application.user.required.UserStatusRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;


  @Override
  public UserResponse create(UserCreateRequest userCreateRequest) {
    UserCreateRequest validRequest = normalizeRequest(userCreateRequest);
    User user = User.create(validRequest);
    UserStatus userStatus = UserStatus.create(user);
    UserStatus savedUserStatus = userStatusRepository.save(userStatus);
    User savedUser = userRepository.save(user);
    return UserResponse.from(savedUser, savedUserStatus);
  }

  @Override
  public UserResponse findById(UUID uuid) {
    User user = findUserOrThrow(uuid);
    UserStatus userStatus = getUserStatusByUserId(uuid);
    return UserResponse.from(user, userStatus);
  }


  @Override
  public UserResponse update(UUID id, UserUpdateRequest userUpdateRequest) {
    User updatedUser = findUserOrThrow(id).update(
        userUpdateRequest.newUsername(),
        userUpdateRequest.newPassword(),
        userUpdateRequest.newEmail());
    User savedUser = userRepository.save(updatedUser);
    UserStatus userStatus = getUserStatusByUserId(savedUser.getId()).refresh(
        updatedUser.getUpdateAt());
    UserStatus savedUserStatus = userStatusRepository.save(userStatus);
    return UserResponse.from(savedUser, savedUserStatus);
  }

  @Override
  public void delete(UUID uuid) {
    findUserOrThrow(uuid);
    UserStatus userStatus = getUserStatusByUserId(uuid);
    userRepository.delete(uuid);
    UUID userStatusUUID = userStatus.getUuid();
    userStatusRepository.delete(userStatusUUID);
  }

  @Override
  public List<UserResponse> readAll() {
    return userRepository.readAll().stream()
        .map(user -> UserResponse.from(
            user, getUserStatusByUserId(user.getId()))).toList();
  }

  private User findUserOrThrow(UUID uuid) {
    return userRepository.findById(uuid).orElseThrow(()
        -> new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND));
  }

  private UserCreateRequest normalizeRequest(UserCreateRequest userCreateRequest) {
    String name = userCreateRequest.username();
    String email = userCreateRequest.email();
    List<User> userList = userRepository.readAll();
    boolean result = userList.stream()
        .noneMatch(m -> m.getEmail().equals(email) || m.getName().equals(name));
    if (!result) {
      throw new DiscodeitRuntimeException(ExceptionType.USER_ALREADY_EXIST);
    }
    return userCreateRequest;
  }

  private UserStatus getUserStatusByUserId(UUID uuid) {
    return userStatusRepository.readAll().stream()
        .filter(status -> status.getUserUuid().equals(uuid))
        .findAny()
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.USER_ALREADY_EXIST));
  }
}