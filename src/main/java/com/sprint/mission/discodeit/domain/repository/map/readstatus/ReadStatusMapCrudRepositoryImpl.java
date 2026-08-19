package com.sprint.mission.discodeit.domain.repository.map.readstatus;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.repository.map.AbstractMapCrudRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ReadStatusMapCrudRepositoryImpl extends AbstractMapCrudRepository<ReadStatus> implements
    ReadStatusRepository {

    @Override
    public List<ReadStatus> findAllEntityByChannelId(UUID channelId) {
        List<ReadStatus> allEntity = super.findAllEntity();
        return allEntity.stream()
            .filter(ReadStatus -> ReadStatus.getChannelId().equals(channelId))
            .toList();
    }

    @Override
    public List<ReadStatus> findAllReadStatusByUserId(UUID userId) {
        List<ReadStatus> allEntity = super.findAllEntity();
        return allEntity.stream()
            .filter(ReadStatus -> ReadStatus.getUserId().equals(userId))
            .toList();
    }

    @Override
    public void deleteReadStatusByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = super.findAllEntity();
        List<ReadStatus> filteredReadStatuses = readStatuses.stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .toList();

        for (ReadStatus filteredReadStatus : filteredReadStatuses) {
            super.deleteEntity(filteredReadStatus.getId());
        }
    }
}
