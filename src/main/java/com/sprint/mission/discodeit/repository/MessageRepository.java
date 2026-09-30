package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  void deleteAllByChannelId(UUID channelId);

  Slice<Message> findAllByChannelId(UUID channelId);

  @Query("SELECT m.createAt "
      + "FROM Message m "
      + "WHERE m.channel.id = :channelId "
      + "ORDER BY m.createAt DESC LIMIT 1")
  Optional<Instant> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

}
