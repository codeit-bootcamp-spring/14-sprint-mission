package com.sprint.mission.discodeit.readstatus.repository;

import com.sprint.mission.discodeit.readstatus.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {

    ReadStatus statusAdd(ReadStatus readStatus);

    ReadStatus findById(UUID readStatusId);

    List<UUID> findByChannelId(UUID channelId);

    List<UUID> findByUserId(UUID userId);

    List<ReadStatus> findByAllUserList(UUID userId);

    void deleteByChannelId(UUID channelId);

    void delete(UUID readStatusId);

    void update(ReadStatus readStatus);
}
