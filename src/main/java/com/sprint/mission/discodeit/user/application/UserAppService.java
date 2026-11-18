package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.application.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.common.DeletionEvent;
import com.sprint.mission.discodeit.message.application.provided.command.MessageCommand;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusCommand;
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
import com.sprint.mission.discodeit.user.application.required.PasswordHasher;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserAppService implements UserRegister, UserModifier, UserRemover, UserStatusCommand {

  private final UserCommand userCommand;
  private final UserEntityFinder entityFinder;
  private final ReadStatusCommand readStatusCommand;
  private final MessageCommand messageCommand;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;
  private final BinaryContentRegister binaryContentRegister;
  private final ApplicationEventPublisher eventPublisher;
  private final PasswordHasher passwordHasher;

  @Override
  public UserDto register(UserCreateRequest request, BinaryContentCreateRequest profile) {
    BinaryContent savedProfile = registerProfile(profile);
    String encodePassword = passwordHasher.hash(request.password());
    User user = userCommand.create(request.username(), request.email(), encodePassword,
        savedProfile);
    log.info("유저 생성 완료: user={}", user);
    return userMapper.toDto(user);
  }


  @Override
  public UserDto modify(UUID userId, UserUpdateRequest request,
      BinaryContentCreateRequest profile) {
    User user = entityFinder.getEntityById(userId);
    if (profile != null) {
      deleteProfile(user);
    }
    BinaryContent savedProfile = registerProfile(profile);
    String encodedPassword = passwordHasher.hash(request.newPassword());
    User updatedUser = userCommand.update(user, request.newUsername(), request.newEmail(),
        encodedPassword,
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

  private void deleteProfile(User user) {
    if (user.getProfile() != null) {
      ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, UUID.class);
      DeletionEvent<List<UUID>> event = new DeletionEvent<>(List.of(user.getProfile().getId()),
          resolvableType);
      eventPublisher.publishEvent(event);
    }
  }

  @Override
  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {
    User user = entityFinder.getEntityById(userId);
    UserStatus refresh = user.refreshActivity(request.newLastActiveAt());
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
