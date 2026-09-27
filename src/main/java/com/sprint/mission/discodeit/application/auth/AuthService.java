package com.sprint.mission.discodeit.application.auth;

import com.sprint.mission.discodeit.application.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.mapper.UserMapper;
import com.sprint.mission.discodeit.application.user.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService implements AuthLogin {

  private final UserEntityFinder userEntityFinder;
  private final UserMapper userMapper;

  public UserDto login(LoginRequest userRequest) {
    String name = userRequest.username();
    String password = userRequest.password();

    User user = userEntityFinder.getEntityByUsername(name);
    if (!user.getPassword().equals(password)) {
      throw new DiscodeitRuntimeException(ExceptionType.INVALID_INFO);
    }
    return userMapper.toDto(user);
  }
}
