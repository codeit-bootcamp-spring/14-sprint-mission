package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStausRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserStatusRepository extends MapFileRepository<UserStatus>
        implements UserStausRepository {

    public FileUserStatusRepository() {
        super(Files.USER_STATUS);
    }

}
