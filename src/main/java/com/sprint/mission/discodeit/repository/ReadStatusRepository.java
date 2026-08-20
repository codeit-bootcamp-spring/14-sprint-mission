package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAll();
    void deleteById(UUID id);

    List<ReadStatus> findAllByChannelId(UUID channelid);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);
}
