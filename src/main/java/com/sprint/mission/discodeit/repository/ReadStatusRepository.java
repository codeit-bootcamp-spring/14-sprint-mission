package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    Optional<ReadStatus> find(UUID id);
    ReadStatus save(ReadStatus readStatus);
    void delete(UUID id);
    void deleteByChannelId(UUID channelId);

    List<ReadStatus> findByUser(UUID userId);

    List<ReadStatus> findByChannel(UUID channelId);
}
