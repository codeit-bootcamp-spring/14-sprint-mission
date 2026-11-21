package com.sprint.mission.discodeit.user.domain.repository;

import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUserId(UUID userId);

    default UserStatus getByUserIdOrThrow(UUID userId){
        return findByUserId(userId).orElseThrow(() -> new CustomException(CustomErrorCode.USER_STATUS_NOT_FOUND_BY_USER_ID));
    }


}
