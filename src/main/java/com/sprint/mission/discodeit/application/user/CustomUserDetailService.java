package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.out.UserRepository;
import com.sprint.mission.discodeit.config.security.CustomUserDetails;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.UserExceptionType;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NullMarked
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(UserExceptionType.USER_NOT_FOUND.getDescription()));

        return new CustomUserDetails(
                UserDto.from(found),
                found.getPassword()
        );
    }
}
