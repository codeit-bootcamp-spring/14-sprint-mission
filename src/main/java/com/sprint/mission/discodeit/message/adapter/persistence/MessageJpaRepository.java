package com.sprint.mission.discodeit.message.adapter.persistence;

import com.sprint.mission.discodeit.message.domain.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findFirstByChannel_IdOrderByCreatedAtDesc(UUID channelId);

  List<Message> findAllByChannel_Id(UUID channelId);

  List<Message> findAllByAuthor_Id(UUID authorId);
}
