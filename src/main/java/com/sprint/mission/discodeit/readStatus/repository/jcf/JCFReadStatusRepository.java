package com.sprint.mission.discodeit.readStatus.repository.jcf;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data;


    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
    }

    @Override
    public List<UUID> findAllByChannelId(UUID ChannelId) {
        return findAll().stream().filter(readStatus -> readStatus.getChannelId().equals(ChannelId))
                .map(ReadStatus::getUserId)
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        // 삭제할 메세지
        List<UUID> ids = findAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(BaseEntity::getId)
                .toList();

        // 진짜 삭제
        for (UUID id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public boolean existsByChannelIdAndUserId(UUID ChannelId, UUID userId) {
        return findAll().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(ChannelId) &&
                        readStatus.getUserId().equals(userId));
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream().filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void update(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
    }
}
