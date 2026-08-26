package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository extends FileRepository<UserStatus>
    implements UserStatusRepository {

    public FileUserStatusRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory
    ) {
        super(fileDirectory,"userStatus");
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }
}
