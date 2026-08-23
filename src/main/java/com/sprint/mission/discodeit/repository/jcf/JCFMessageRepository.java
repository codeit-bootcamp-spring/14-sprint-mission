package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFMessageRepository extends AbstractJCFRepository<Message>
        implements MessageRepository {

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateContent(UUID id, String content) {
        findById(id).ifPresent(retrieved -> retrieved.updateContent(content));
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        findAll().stream()
                .filter(message -> message.getUserId().equals(userId))
                .map(message -> message.getId())
                .forEach(toBeDeleted -> deleteById(toBeDeleted));
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(message -> message.getId())
                .forEach(toBeDeleted -> deleteById(toBeDeleted));
    }

    @Override
    public Optional<Instant> findLatestMessageByChannelId(UUID channelId) {
        return findAllByChannelId(channelId).stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder());
    }
}
