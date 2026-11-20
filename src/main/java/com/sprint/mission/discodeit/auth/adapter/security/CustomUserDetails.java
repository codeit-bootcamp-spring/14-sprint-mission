package com.sprint.mission.discodeit.auth.adapter.security;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
public final class CustomUserDetails implements UserDetails, CredentialsContainer {

  @Serial
  private static final long serialVersionUID = 1L;


  private final UUID userId;
  private final String username;
  private @Nullable String password;
  private final List<GrantedAuthority> authorities;

  public CustomUserDetails(UUID userId, String username, @Nullable String password,
      List<GrantedAuthority> authorities) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.authorities = List.copyOf(authorities);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CustomUserDetails that)) {
      return false;
    }

    return getUserId().equals(that.getUserId());
  }

  @Override
  public int hashCode() {
    return getUserId().hashCode();
  }

  public UUID getUserId() {
    return userId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public @Nullable String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void eraseCredentials() {
    password = null;
  }
}
