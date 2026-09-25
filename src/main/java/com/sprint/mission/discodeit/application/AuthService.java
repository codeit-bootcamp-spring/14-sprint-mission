package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.adaptor.dto.userDto.LoginRequest;
import com.sprint.mission.discodeit.application.user.required.UserRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserRepository userRepository;

  public User login(LoginRequest userRequest) {
    String name = userRequest.username();
    String password = userRequest.password();

    User user = userRepository.readAll().stream()
        .filter(m -> m.getName().equals(name))
        .findAny()
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND));
    if (!user.getPassword().equals(password)) {
      throw new DiscodeitRuntimeException(ExceptionType.INVALID_INFO);
    }
    return user;
  }
}
