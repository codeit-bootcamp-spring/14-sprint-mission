package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.channel.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<ChannelRepository, UUID> {
    Channel updateNameAndDescription(UUID id, String name, String description);

    void update(UUID id, Instant newUpdatedAt);
}
