package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.message.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface MessageRepository
        extends JpaRepository<Message, UUID>, MessageRepositoryCustom {
    void deleteAllByChannel_Id(UUID channelId);

    @Query("""
            SELECT m FROM Message m
            WHERE m.channel.id=:channelId AND m.createdAt < :cursor""")
    Slice<Message> findAllByChannelId(
            UUID channelId,
            Instant cursor,
            Pageable pageable
    );
}
