package com.sprint.mission.discodeit.readstatus.repository;

import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public ReadStatus statusAdd(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        return readStatusMap.get(readStatusId);
    }

    @Override
    public List<UUID> findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .map(ReadStatus::getUserId)
            .toList();
    }

    @Override
    public List<UUID> findByUserId(UUID userId) {
        return readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .map(ReadStatus::getChannelId)
            .toList();
    }

    @Override
    public List<ReadStatus> findByAllUserList(UUID userId) {
        return readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> readStatusId = readStatusMap.values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .map(ReadStatus::getId)
            .toList();

        readStatusId.forEach(readStatusMap::remove);
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusMap.remove(readStatusId);
    }

    @Override
    public void update(ReadStatus readStatus) {
        readStatusMap.replace(readStatus.getId(), readStatus);
    }
}
