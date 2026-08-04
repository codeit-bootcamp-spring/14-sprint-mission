package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus>
        implements ReadStatusRepository {
    protected FileReadStatusRepository() {
        super(Files.READ_STATUS);
    }

    @Override
    public List<ReadStatus> createAll(List<ReadStatus> readStatuses) {
        List<ReadStatus> created = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            created.add(super.create(readStatus));
        }

        return created;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return super.buffer.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }
}
