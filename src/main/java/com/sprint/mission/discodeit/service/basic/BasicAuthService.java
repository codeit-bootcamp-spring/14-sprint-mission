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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    log.info("로그인 요청 - 입력 이름: {}", loginRequest.username());
    String username = loginRequest.username();
    String password = loginRequest.password();

    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UserNotFoundByUsernameException(username));

    if (!user.getPassword().equals(password)) {
      throw new InvalidPasswordException(user.getUsername());
    }

    return userMapper.toDto(user, null);
  }
}
