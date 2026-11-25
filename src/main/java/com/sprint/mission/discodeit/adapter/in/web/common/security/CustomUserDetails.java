package com.sprint.mission.discodeit.adapter.in.web.common.security;

import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public final class CustomUserDetails
        extends UserSessionKey
        implements UserDetails {

    private final UserResult user;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserResult user, String password) {
        super(user.id());
        this.user = user;
        this.password = password;
        this.authorities = getAuthorityFrom(user.role());
    }

    private Collection<SimpleGrantedAuthority> getAuthorityFrom(String role) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        return List.of(authority);
    }

    public UUID getUserId() {
        return super.getUserId();
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
}
