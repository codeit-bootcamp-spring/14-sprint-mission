package com.sprint.mission.discodeit.core.application.user.service;

import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.config.security.CustomUserDetails;
import com.sprint.mission.discodeit.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserPort users;

    @Override
    @NullMarked
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = users.get(username);

        return new CustomUserDetails(
                UserResult.from(found, false),
                found.getPassword()
        );
    }
}
