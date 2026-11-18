package com.sprint.mission.discodeit.auth.security;

import com.sprint.mission.discodeit.user.dto.UserDto;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {

    private final UserDto userDto;
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE: " + userDto.role().name()));
    }

    @Override
    public String getUsername() {
        return userDto.userName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscodeitUserDetails that)) {
            return false;
        }
        return userDto.id().equals(that.userDto.id());
    }

    @Override
    public int hashCode() {
        return userDto.id().hashCode();
    }
}
