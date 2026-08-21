package com.sprint.mission.discodeit.repository.file;

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
        havingValue = "file"
)
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus>
        implements UserStatusRepository {

    public FileUserStatusRepository() {
        super(Files.USER_STATUS);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return super.buffer.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public UserStatus deleteByUserId(UUID userId) {
        UserStatus deleting = findByUserId(userId).orElse(null);
        UserStatus deleted = deleteById(deleting.getId());
        super.writeFromBufferToFile();
        return deleted;
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findAll().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public UserStatus updateLastActiveAtByUserId(UUID userId, Instant newLastActiveAt) {
        UserStatus updating = findByUserId(userId).orElseThrow();
        UserStatus updated = updating.updateLastActiveAt(newLastActiveAt);
        super.writeFromBufferToFile();
        return updated;
    }
}
