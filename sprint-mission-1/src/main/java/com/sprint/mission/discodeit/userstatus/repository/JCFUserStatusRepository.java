package com.sprint.mission.discodeit.userstatus.repository;

import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> userStatusMap = new HashMap<>();

    @Override
    public UserStatus statusAdd(UserStatus userStatus) {
        userStatusMap.put(userStatus.getUserStatusId(), userStatus);
        return userStatusMap.get(userStatus.getUserStatusId());
    }

    @Override
    public void delete(UserStatus userStatus) {
        userStatusMap.remove(userStatus.getUserStatusId());
    }

    @Override
    public void update(UserStatus userStatus) {
        userStatusMap.replace(userStatus.getUserStatusId(), userStatus);
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusMap.get(userStatusId);
    }

    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusMap.values().stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }

    public List<UserStatus> findAll() {
        return userStatusMap.values().stream().toList();
    }
}
