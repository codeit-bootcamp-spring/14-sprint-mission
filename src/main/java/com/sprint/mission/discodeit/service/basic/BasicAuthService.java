package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.auth.InvalidPasswordException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundByUsernameException;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UserNotFoundByUsernameException(username));

    if (!user.getPassword().equals(password)) {
      throw new InvalidPasswordException(user.getPassword());
    }

    return userMapper.toDto(user, null);
  }
}
