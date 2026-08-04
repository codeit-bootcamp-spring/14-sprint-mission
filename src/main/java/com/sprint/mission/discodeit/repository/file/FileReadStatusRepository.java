package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;


public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus>
        implements ReadStatusRepository {
    protected FileReadStatusRepository() {
        super(Files.READ_STATUS);
    }
}
