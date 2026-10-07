package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.mapper.UserMapper;
import com.sprint.mission.discodeit.user.application.provided.query.UserAllFinder;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.application.provided.query.UserFinder;
import com.sprint.mission.discodeit.user.application.required.UserRepository;
import com.sprint.mission.discodeit.user.domain.User;
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
        .orElseThrow(() -> new DiscodeitRuntimeException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public List<User> getEntitiesById(List<UUID> userIds) {
    return userRepository.findAllById(userIds);
  }

  @Override
  public User getEntityByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new DiscodeitRuntimeException(ErrorCode.USER_NOT_FOUND));
  }


  @Override
  public UserDto getById(UUID userId) {
    return userMapper.toDto(getEntityById(userId));
  }
}
