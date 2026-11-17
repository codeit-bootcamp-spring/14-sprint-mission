package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.User;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final UserDto userDto;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.userDto = UserDto.from(user);
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return userDto.username();
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
