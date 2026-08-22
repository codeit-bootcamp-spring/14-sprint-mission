package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplication {
    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserDto login(String name, String password) {
        userService.validateExistsByName(name);
        User retrieved = userService.findByNameAndPassword(name, password);
        UserStatus userStatus = userStatusService.findByUserId(retrieved.getId());
        userStatus.updateLastSeenAt();
        return UserDto.of(retrieved, userStatus);
    }
}
