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
    public boolean existsByUserAndChannel(UUID userId, UUID channelId) {
        return super.findAll().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId)
                        && readStatus.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> createAll(List<ReadStatus> readStatuses) {
        List<ReadStatus> created = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            created.add(super.create(readStatus));
        }
        super.writeFromBufferToFile();
        return created;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return super.buffer.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        super.buffer.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .forEach(readStatus -> super.deleteById(readStatus.getId()));
        super.writeFromBufferToFile();
    }

    @Override
    public void update(UUID id) {
        findById(id).ifPresent(readStatus -> readStatus.update());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .forEach(readStatus -> deleteById(readStatus.getId()));
    }
}
