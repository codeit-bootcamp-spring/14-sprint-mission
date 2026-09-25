package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.domain.Message;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findAllByChannel_Id(UUID channelId, Pageable pageable);

  Optional<Message> findFirstByChannel_IdOrderByCreatedAtDesc(UUID channelId);
}
