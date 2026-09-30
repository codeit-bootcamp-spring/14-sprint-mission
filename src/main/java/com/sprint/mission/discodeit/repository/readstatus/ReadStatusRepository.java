package com.sprint.mission.discodeit.repository.readstatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID>, ReadStatusRepositoryCustom{
    boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUser_Id(UUID userId);

    ReadStatus findByUser_Id(UUID userId);

    ReadStatus findByChannel_Id(UUID channelId);
}
