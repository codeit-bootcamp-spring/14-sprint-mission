package com.sprint.mission.discodeit.domain.service.readstatus;

import com.sprint.mission.discodeit.domain.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(ReadStatus readStatus);
    ReadStatus findReadStatusById(UUID readStatusId);
    List<ReadStatus> findAllReadStatusByChannelId(UUID channelId);
    List<ReadStatus> findReadStatusByUserId(UUID userId);
    void deleteReadStatusById(UUID readStatusId);
    void deleteReadStatusByChannelId(UUID channelId);
    ReadStatus updateReadStatusReadTime(UUID readStatusId);
}
