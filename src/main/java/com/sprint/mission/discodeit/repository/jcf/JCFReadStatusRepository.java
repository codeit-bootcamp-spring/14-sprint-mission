package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {

    protected Map<UUID, ReadStatus> data;

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .toList();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
            .anyMatch(readStatus -> readStatus.getUserId().equals(userId) &&
                readStatus.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .toList();
    }
}
