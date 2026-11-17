package com.sprint.mission.discodeit.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public final class UserRoles {

    static final GrantedAuthority USER_ROLE = new SimpleGrantedAuthority("USER_ROLE");

    static final List<GrantedAuthority> USER_ROLES = List.of(USER_ROLE);
}
