package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    List<ReadStatus> findAllByChannel_Id(UUID channelId);

    List<ReadStatus> findAllByUser_Id(UUID userId);

    boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);
}
