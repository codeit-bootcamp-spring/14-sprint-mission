package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
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
    public void deleteByUserId(UUID userId) {
        super.buffer.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .map(userStatus -> userStatus.getId())
                .forEach(id -> deleteById(id));
    }
}
