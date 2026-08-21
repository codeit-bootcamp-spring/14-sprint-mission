package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplication {
    private final UserService userService;
    private final UserStatusRepository userStatusRepository;

    public UserDto login(String name, String password) {
        userService.validateExistsByName(name);
        User retrieved = userService.findByNameAndPassword(name, password);
        UserStatus userStatus = userStatusRepository.findByUserId(retrieved.getId())
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));
        userStatus.updateLastSeenAt();
        return UserDto.of(retrieved, userStatus);
    }
}
