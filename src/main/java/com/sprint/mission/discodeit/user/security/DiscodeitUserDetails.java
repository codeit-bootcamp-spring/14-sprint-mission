package com.sprint.mission.discodeit.user.security;

import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DiscodeitUserDetails implements UserDetails {

    final SecurityUserInfo user;
    final String password;

    public static DiscodeitUserDetails init(SecurityUserInfo user, String password) {
        return DiscodeitUserDetails.builder()
            .user(user)
            .password(password)
            .build();
    }

    @Override
    public String getUsername() {
        return user.username();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.role().name()));
    }
}
