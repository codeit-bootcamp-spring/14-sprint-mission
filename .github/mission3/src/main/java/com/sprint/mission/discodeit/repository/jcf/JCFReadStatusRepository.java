package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    @Override
    public Optional<ReadStatus> find(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return data.put(readStatus.getUuid(), readStatus);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> list = data.values().stream().filter(readStatus -> readStatus.getChannelId().equals(channelId)).map(ReadStatus::getUuid).toList();
        for (UUID id : list){
            data.remove(id);
        }
    }

    @Override
    public List<ReadStatus> findByUser(UUID userId) {
        return data.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId)).toList();
    }

    @Override
    public List<ReadStatus> findByChannel(UUID channelId) {
        return data.values().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId)).toList();
    }
}
