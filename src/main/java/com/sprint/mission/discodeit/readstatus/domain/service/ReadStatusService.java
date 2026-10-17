package com.sprint.mission.discodeit.readstatus.domain.service;

import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(ReadStatus readStatus);
}
