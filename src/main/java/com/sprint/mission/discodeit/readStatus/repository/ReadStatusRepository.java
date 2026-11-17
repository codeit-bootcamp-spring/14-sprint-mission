package com.sprint.mission.discodeit.readStatus.repository;

import com.sprint.mission.discodeit.readStatus.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


    boolean existsByChannelIdAndUserId(UUID ChannelId, UUID userId);

    @Query("Select rs.user.id from  ReadStatus rs where rs.channel.id = :channelId")
    List<UUID> findUserIdByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);
}
