package com.sprint.mission.discodeit.repository.readstatus;

import com.sprint.mission.discodeit.domain.readstatus.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID>, ReadStatusRepositoryCustom{
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus findByUserId(UUID userId);

    ReadStatus findByChannelId(UUID channelId);
}
