package com.sprint.mission.discodeit.domain.service.userstatus;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UserStatus userStatus);
    UserStatus findUserStatus(UUID userStatusId);
    List<UserStatus> findAllUserStatus();
    UserStatus updateUserStatusByUserId(UUID userId);
    UserStatus updateUserStatusByUserId(UUID userId, Instant activeAt);
    void deleteUserStatusByUserId(UUID userId);

    UserStatus findUserStatusByUserId(UUID userId);
}
