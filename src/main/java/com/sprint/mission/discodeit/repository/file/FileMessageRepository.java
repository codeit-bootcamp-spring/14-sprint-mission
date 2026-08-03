package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileMessageRepository extends MapFileRepository<Message>
        implements MessageRepository {

    public FileMessageRepository() {
        super(Files.MESSAGE);
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
}
