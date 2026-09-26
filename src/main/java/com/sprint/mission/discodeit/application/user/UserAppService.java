package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.application.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.application.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.application.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.application.user.mapper.UserMapper;
import com.sprint.mission.discodeit.application.user.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.application.user.provided.command.UserCommand;
import com.sprint.mission.discodeit.application.user.provided.command.UserModifier;
import com.sprint.mission.discodeit.application.user.provided.command.UserRegister;
import com.sprint.mission.discodeit.application.user.provided.command.UserRemover;
import com.sprint.mission.discodeit.application.user.provided.command.UserStatusCommand;
import com.sprint.mission.discodeit.application.user.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAppService implements UserRegister, UserModifier, UserRemover, UserStatusCommand {

  private final UserCommand userCommand;
  private final UserEntityFinder entityFinder;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserDto register(UserCreateRequest request) {
    User user = userCommand.create(request.username(), request.email(), request.password(), null);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto modify(UUID userId, UserUpdateRequest request) {
    User user = entityFinder.getEntityById(userId);
    User updatedUser = userCommand.update(user, request.newUsername(), request.newEmail(),
        request.newPassword(),
        null);
    return userMapper.toDto(updatedUser);
  }

  @Override
  public void delete(UUID userId) {
    User user = entityFinder.getEntityById(userId);
    userCommand.delete(user);

  }

  @Override
  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {
    User user = entityFinder.getEntityById(userId);
    UserStatus refresh = user.getUserStatus().refresh(request.newLastActiveAt());
    return userStatusMapper.toDto(refresh);
  }
}
