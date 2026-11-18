package com.sprint.mission.discodeit.message.application.required;

import com.sprint.mission.discodeit.message.domain.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {


  Optional<Message> findFirstByChannel_IdOrderByCreatedAtDesc(UUID channelId);

  List<Message> findAllByChannel_Id(UUID channelId);

  List<Message> findAllByAuthor_Id(UUID authorId);

  List<Message> findAllByCursor(
      UUID channelId,
      Instant cursor,
      UUID idAfter,
      int limit
  );

  Optional<Message> findById(UUID messageId);

  Message save(Message message);

  void delete(Message message);

  void deleteAll(List<Message> messages);
}
