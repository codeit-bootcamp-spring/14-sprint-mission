package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFReadStatusRepository extends AbstractJCFRepository<ReadStatus>
        implements ReadStatusRepository {
    @Override
    public boolean existsByUserAndChannel(UUID userId, UUID channelId) {
        return findAll().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId)
                        && readStatus.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> createAll(List<ReadStatus> readStatuses) {
        for (ReadStatus readStatus : readStatuses) {
            super.create(readStatus);
        }
        return readStatuses;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
       return findAll().stream()
               .filter(readStatus -> readStatus.getChannelId().equals(channelId))
               .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        findAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .forEach(readStatus -> deleteById(readStatus.getId()));
    }

    @Override
    public List<ReadStatus> updateByChannelId(UUID channelId, Instant newLastReadAt) {
        List<ReadStatus> channelReadStatus = super.STORE.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
        for (ReadStatus eachReadStatus : channelReadStatus) {
            eachReadStatus.update(newLastReadAt);
        }
        return channelReadStatus;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .forEach(readStatus -> deleteById(readStatus.getId()));
    }

    @Override
    public List<UUID> findAllUserIdsByChannelId(UUID channelId) {
        return findAllByChannelId(channelId).stream()
                .map(readStatus -> readStatus.getUserId())
                .toList();
    }

    @Override
    public ReadStatus update(UUID publicReadStatusId, Instant newLastReadAt) {
        return super.STORE.get(publicReadStatusId).update(newLastReadAt);
    }

    @Override
    public boolean existsById(UUID id) {
        return super.STORE.values().stream()
                .anyMatch(readStatus -> readStatus.getId().equals(id));
    }
}
