package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthApplication {
    private final UserService userService;

    public UserDto login(String name, String password) {
        User retrieved = userService.findByNameAndPassword(name, password);
        retrieved.updateLastActiveAt(Instant.now());
        return UserDto.of(retrieved);
    }
}
