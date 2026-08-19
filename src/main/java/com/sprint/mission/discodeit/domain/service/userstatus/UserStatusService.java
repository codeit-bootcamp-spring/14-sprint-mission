package com.sprint.mission.discodeit.domain.service.userstatus;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UserStatus userStatus);
    UserStatus findUserStatus(UUID userStatusId);
    List<UserStatus> findAllUserStatus();
    UserStatus updateUserStatusByUserId(UUID userId);
    void deleteUserStatusByUserId(UUID userId);
}
