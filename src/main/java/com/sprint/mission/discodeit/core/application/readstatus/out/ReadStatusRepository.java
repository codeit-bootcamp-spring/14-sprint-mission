package com.sprint.mission.discodeit.core.application.readstatus.out;

import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID>, ReadStatusRepositoryCustom{
    boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);

    void deleteAllByUser_Id(UUID userId);

    void deleteAllByChannel_Id(UUID channelId);
}
