package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserStatusDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusApplication {
    private final UserStatusService userStatusService;
    private final UserService userService;

    public UserStatus create(UUID userId) {
        userService.validateExistsById(userId);
        userStatusService.validateUserIdAvailable(userId);
        UserStatus userStatus = new UserStatus(userId);
        return userStatusService.create(userStatus);
    }

    public UserStatus getUserStatus(UUID id) {
        return userStatusService.findById(id);
    }

    public List<UserStatus> getAllUserStatus() {
        return userStatusService.findAll();
    }

    public UserStatusDto updateByUserId(UUID userId, Instant newLastActiveAt) {
        UserStatus updatedUserStatus = userStatusService.updateLastActiveAtByUserId(userId, newLastActiveAt);
        return UserStatusDto.of(updatedUserStatus);
    }

    public void delete(UUID id) {
        userStatusService.deleteById(id);
    }

}
