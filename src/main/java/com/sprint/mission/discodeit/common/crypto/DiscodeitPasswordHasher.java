package com.sprint.mission.discodeit.common.crypto;

import com.sprint.mission.discodeit.user.application.required.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DiscodeitPasswordHasher implements PasswordHasher {

  private final PasswordEncoder passwordEncoder;


  @Override
  public String hash(String rawPwd) {
    return passwordEncoder.encode(rawPwd);
  }

  @Override
  public boolean matches(String rawPwd, String hashPwd) {
    return passwordEncoder.matches(rawPwd, hashPwd);
  }
}
