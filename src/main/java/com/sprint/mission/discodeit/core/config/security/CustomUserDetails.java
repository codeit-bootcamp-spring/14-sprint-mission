package com.sprint.mission.discodeit.core.config.security;

import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
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
    private final UserResult user;

    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserResult user, String password) {
        this.user = user;
        this.password = password;
        this.authorities = getAuthorityFrom(user.role());
    }

    private Collection<SimpleGrantedAuthority> getAuthorityFrom(String role) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        return List.of(authority);
    }

    public UUID getUserId() {
        return user.id();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return user.username();
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
