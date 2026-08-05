package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class FileMessageRepository extends AbstractFileRepository<Message>
        implements MessageRepository {

    public FileMessageRepository() {
        super(Files.MESSAGE);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return super.buffer.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateContent(UUID id, String content) {
        findById(id).ifPresent(retrieved -> {
            retrieved.updateContent(content);
            super.writeFromBufferToFile();
        });
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        buffer.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .map(message -> message.getId())
                .forEach(toBeDeleted -> deleteById(toBeDeleted));
        super.writeFromBufferToFile();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        buffer.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(message -> message.getId())
                .forEach(toBeDeleted -> deleteById(toBeDeleted));
        super.writeFromBufferToFile();
    }

    // Channel에 message 없으면 null 반환할 가능성 있음
    @Override
    public Optional<Instant> findLatestMessageByChannelId(UUID channelId) {
        return findAllByChannelId(channelId).stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder());
    }
}
