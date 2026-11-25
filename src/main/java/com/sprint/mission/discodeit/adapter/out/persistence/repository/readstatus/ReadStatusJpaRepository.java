package com.sprint.mission.discodeit.adapter.out.persistence.repository.readstatus;

import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadStatusJpaRepository extends JpaRepository<ReadStatus, UUID>, ReadStatusQueryRepository {
    boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);

    void deleteAllByUser_Id(UUID userId);

    void deleteAllByChannel_Id(UUID channelId);
}
