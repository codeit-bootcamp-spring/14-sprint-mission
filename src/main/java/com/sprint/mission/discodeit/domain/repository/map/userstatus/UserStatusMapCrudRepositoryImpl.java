package com.sprint.mission.discodeit.domain.repository.map.userstatus;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.domain.repository.map.AbstractMapCrudRepository;
import com.sprint.mission.discodeit.domain.repository.UserStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UserStatusMapCrudRepositoryImpl extends AbstractMapCrudRepository<UserStatus> implements
    UserStatusRepository {

    @Override
    public Optional<UserStatus> findUserStatusByUserId(UUID userId) {
        List<UserStatus> userStatusList = super.findAllEntity();
        return userStatusList.stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }
}
