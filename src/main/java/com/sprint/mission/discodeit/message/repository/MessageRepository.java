package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    void deleteByChannelId(UUID channelId);

    @EntityGraph(attributePaths = {"author", "author.profile", "author.userStatus"})
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
    List<Message> findAllByChannelId(UUID channelId);
}
