package com.sprint.mission.discodeit.auth.adapter.security;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserEntityFinder userEntityFinder;

  @Override
  public UserDetails loadUserByUsername(
      String username) {
    try {
      User user = userEntityFinder.getEntityByUsername(username);

      return CustomUserDetails.builder()
          .userId(user.getId())
          .username(user.getUsername())
          .password(user.getPassword().password())
          .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
          .build();
    } catch (DiscodeitRuntimeException e) {
      if (e.getErrorCode() == ErrorCode.USER_NOT_FOUND) {
        throw new UsernameNotFoundException("사용자 정보를 다시 확인해주세요.", e);
      }
      throw e;
    }
  }
}