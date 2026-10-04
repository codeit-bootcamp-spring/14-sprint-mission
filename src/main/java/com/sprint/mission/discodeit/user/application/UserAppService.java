package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.application.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.application.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.dto.UserStatusDto;
import com.sprint.mission.discodeit.user.application.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.application.mapper.UserMapper;
import com.sprint.mission.discodeit.user.application.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.user.application.provided.command.UserCommand;
import com.sprint.mission.discodeit.user.application.provided.command.UserModifier;
import com.sprint.mission.discodeit.user.application.provided.command.UserRegister;
import com.sprint.mission.discodeit.user.application.provided.command.UserRemover;
import com.sprint.mission.discodeit.user.application.provided.command.UserStatusCommand;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import java.util.List;
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
  private final BinaryContentRegister binaryContentRegister;

  @Override
  public UserDto register(UserCreateRequest request, BinaryContentCreateRequest profile) {
    BinaryContent savedProfile = registerProfile(profile);
    User user = userCommand.create(request.username(), request.email(), request.password(),
        savedProfile);
    return userMapper.toDto(user);
  }


  @Override
  public UserDto modify(UUID userId, UserUpdateRequest request,
      BinaryContentCreateRequest profile) {
    User user = entityFinder.getEntityById(userId);
    BinaryContent savedProfile = registerProfile(profile);
    User updatedUser = userCommand.update(user, request.newUsername(), request.newEmail(),
        request.newPassword(),
        savedProfile);
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

  private BinaryContent registerProfile(BinaryContentCreateRequest profile) {
    if (profile == null) {
      return null;
    }
    List<BinaryContent> savedProfiles = binaryContentRegister.register(List.of(profile));
    return savedProfiles.getFirst();
  }
}
