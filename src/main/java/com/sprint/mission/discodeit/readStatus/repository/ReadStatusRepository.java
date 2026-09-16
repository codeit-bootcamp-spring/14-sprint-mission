package com.sprint.mission.discodeit.readStatus.repository;

import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


    boolean existsByChannelIdAndUserId(UUID ChannelId, UUID userId);

    List<UUID> findAllByChannelId(UUID channelId);

    Optional<ReadStatus> findAllByUserId(UUID userId);
}
