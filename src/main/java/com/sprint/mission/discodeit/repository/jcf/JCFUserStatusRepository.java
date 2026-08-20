package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFUserStatusRepository extends AbstractJCFRepository<UserStatus>
        implements UserStatusRepository {
    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .map(userStatus -> userStatus.getId())
                .forEach(id -> deleteById(id));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findAll().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public UserStatus updateLastActiveAtByUserId(UUID userId, Instant newLastActiveAt) {
        UserStatus updating = super.STORE.get(userId);
        return updating.updateLastActiveAt(newLastActiveAt);
    }
}
