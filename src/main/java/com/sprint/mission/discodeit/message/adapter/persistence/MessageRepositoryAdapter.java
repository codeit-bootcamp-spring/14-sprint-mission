package com.sprint.mission.discodeit.message.adapter.persistence;

import com.sprint.mission.discodeit.message.application.required.MessageRepository;
import com.sprint.mission.discodeit.message.domain.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepository {

  private final MessageJpaRepository jpaRepository;
  private final MessageQRepository qRepository;

  @Override
  public Optional<Message> findFirstByChannel_IdOrderByCreatedAtDesc(UUID channelId) {
    return jpaRepository.findFirstByChannel_IdOrderByCreatedAtDesc(channelId);
  }

  @Override
  public List<Message> findAllByChannel_Id(UUID channelId) {
    return jpaRepository.findAllByChannel_Id(channelId);
  }

  @Override
  public List<Message> findAllByAuthor_Id(UUID authorId) {
    return jpaRepository.findAllByAuthor_Id(authorId);
  }

  @Override
  public List<Message> findAllByCursor(UUID channelId, Instant cursor, UUID idAfter, int limit) {
    return qRepository.findAllByCursor(channelId, cursor, idAfter, limit);
  }

  @Override
  public Optional<Message> findById(UUID messageId) {
    return jpaRepository.findById(messageId);
  }

  @Override
  public Message save(Message message) {
    return jpaRepository.save(message);
  }

  @Override
  public void delete(Message message) {
    jpaRepository.delete(message);
  }

  @Override
  public void deleteAll(List<Message> messages) {
    jpaRepository.deleteAll(messages);
  }
}
