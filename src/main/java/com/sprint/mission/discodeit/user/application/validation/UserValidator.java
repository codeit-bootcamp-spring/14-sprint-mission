package com.sprint.mission.discodeit.user.application.validation;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.user.application.required.UserRepository;
import com.sprint.mission.discodeit.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public final class UserValidator {

  private final UserRepository userRepository;


  public void validateCreate(String username, String email) {
    validateUsername(username);
    validateEmail(email);
  }

  public void validateUpdate(User user, String username, String email) {
    validateChangeName(user, username);
    validateChangeEmail(user, email);
  }

  private void validateUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new DiscodeitRuntimeException(ErrorCode.USER_ALREADY_EXIST);
    }
  }

  private void validateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DiscodeitRuntimeException(ErrorCode.USER_ALREADY_EXIST);
    }
  }

  private void validateChangeName(User user, String username) {
    if (username == null) {
      return;
    }
    if (username.equals(user.getUsername())) {
      return;
    }
    validateUsername(username);
  }

  private void validateChangeEmail(User user, String email) {
    if (email == null || email.equals(user.getEmail())) {
      return;
    }
    validateEmail(email);
  }

}
