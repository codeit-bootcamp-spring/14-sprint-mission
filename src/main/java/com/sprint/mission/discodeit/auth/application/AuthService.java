package com.sprint.mission.discodeit.auth.application;

import com.sprint.mission.discodeit.auth.application.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.application.provided.AuthLogin;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.mapper.UserMapper;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
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
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }
    return userMapper.toDto(user);
  }
}
