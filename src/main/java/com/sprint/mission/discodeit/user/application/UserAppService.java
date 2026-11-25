package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.application.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.common.DeletionEvent;
import com.sprint.mission.discodeit.message.application.provided.command.MessageCommand;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.dto.UserRoleChanged;
import com.sprint.mission.discodeit.user.application.dto.UserStatusDto;
import com.sprint.mission.discodeit.user.application.mapper.UserMapper;
import com.sprint.mission.discodeit.user.application.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.user.application.provided.command.ModifyUserCommand;
import com.sprint.mission.discodeit.user.application.provided.command.RefreshUserStatusCommand;
import com.sprint.mission.discodeit.user.application.provided.command.RegisterUserCommand;
import com.sprint.mission.discodeit.user.application.provided.command.UserCommand;
import com.sprint.mission.discodeit.user.application.provided.command.UserModifier;
import com.sprint.mission.discodeit.user.application.provided.command.UserRegister;
import com.sprint.mission.discodeit.user.application.provided.command.UserRemover;
import com.sprint.mission.discodeit.user.application.provided.command.UserRoleModifier;
import com.sprint.mission.discodeit.user.application.provided.command.UserStatusCommand;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.Role;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserAppService implements UserRegister, UserModifier, UserRemover, UserStatusCommand,
    UserRoleModifier {

  private final UserCommand userCommand;
  private final UserEntityFinder entityFinder;
  private final ReadStatusCommand readStatusCommand;
  private final MessageCommand messageCommand;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;
  private final BinaryContentRegister binaryContentRegister;
  private final ApplicationEventPublisher eventPublisher;


  @Override
  public UserDto register(RegisterUserCommand command) {
    BinaryContent savedProfile = registerProfile(command.profile());
    User user = userCommand.create(command.username(), command.email(), command.password(),
        savedProfile);
    log.info("유저 생성 완료: user={}", user);
    return userMapper.toDto(user);
  }


  @Override
  public UserDto modify(ModifyUserCommand command) {
    User user = entityFinder.getEntityById(command.userId());
    if (command.profile() != null) {
      deleteProfile(user);
    }
    BinaryContent savedProfile = registerProfile(command.profile());
    String password = command.newPassword();
    User updatedUser = userCommand.update(user, command.newUsername(), command.newEmail(),
        password,
        savedProfile);
    return userMapper.toDto(updatedUser);
  }

  @Override
  public void delete(UUID userId) {
    User user = entityFinder.getEntityById(userId);
    readStatusCommand.deleteAllByUserId(userId);
    messageCommand.clearAuthorByUserId(userId);
    deleteProfile(user);
    userCommand.delete(user);

  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public UserDto updateRole(UUID userId, Role role) {
    User user = entityFinder.getEntityById(userId);

    if (user.getRole() != role) {
      user.updateRole(role);
      eventPublisher.publishEvent(UserRoleChanged.to(userId));
    }

    return userMapper.toDto(user);
  }

  private void deleteProfile(User user) {
    if (user.getProfile() != null) {
      ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, UUID.class);
      DeletionEvent<List<UUID>> event = new DeletionEvent<>(List.of(user.getProfile().getId()),
          resolvableType);
      eventPublisher.publishEvent(event);
    }
  }

  @Override
  public UserStatusDto update(RefreshUserStatusCommand command) {
    User user = entityFinder.getEntityById(command.userId());
    UserStatus refresh = user.refreshActivity(command.lastActiveAt());
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
