package com.sprint.mission.discodeit.repository.readstatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID>, ReadStatusRepositoryCustom{
    boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);

    ReadStatus findByUser_Id(UUID userId);

    ReadStatus findByChannel_Id(UUID channelId);
}
