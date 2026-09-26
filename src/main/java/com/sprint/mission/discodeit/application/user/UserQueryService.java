package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.mapper.UserMapper;
import com.sprint.mission.discodeit.application.user.provided.query.UserAllFinder;
import com.sprint.mission.discodeit.application.user.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.application.user.provided.query.UserFinder;
import com.sprint.mission.discodeit.application.user.required.UserRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService implements UserFinder, UserAllFinder, UserEntityFinder {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public List<UserDto> getAll() {
    return userRepository.findAll().stream().map(userMapper::toDto).toList();
  }

  @Override
  public User getEntityById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND));
  }

  @Override
  public List<User> getEntitiesById(List<UUID> userIds) {
    return userRepository.findAllById(userIds);
  }

  @Override
  public User getEntityByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND));
  }


  @Override
  public UserDto getById(UUID userId) {
    return userMapper.toDto(getEntityById(userId));
  }
}
