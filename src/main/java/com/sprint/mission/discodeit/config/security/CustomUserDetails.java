package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.domain.user.Role;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final UserDto userDto;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserDto userDto, String password) {
        this.userDto = userDto;
        this.password = password;
        this.authorities = getAuthorityFrom(userDto.role());
    }

    private Collection<SimpleGrantedAuthority> getAuthorityFrom(Role role) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return List.of(authority);
    }

    public UUID getUserId() {
        return userDto.id();
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomUserDetails customUserDetails)) {
            return false;

        }

        return Objects.equals(customUserDetails.getUserId(), this.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}
