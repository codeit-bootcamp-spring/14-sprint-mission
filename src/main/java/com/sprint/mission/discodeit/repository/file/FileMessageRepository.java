package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileMessageRepository extends MapFileIO<Message>
        implements MessageRepository {

    private final Map<UUID, Message> EMPTY_BUFFER = new HashMap<>();
    private Map<UUID, Message> buffer;

    public FileMessageRepository() {
        super(Files.MESSAGE);
        this.buffer = Optional.of(file)
                .filter(file -> file.exists() && file.length() > 0)
                .map(file -> super.readFile())
                .orElseGet(() -> super.writeFile(EMPTY_BUFFER));
    }

    @Override
    public Message create(Message message) {
        UUID id = message.getId();
        return findById(id).orElseGet(() -> {
            buffer.put(id, message);
            super.writeFile(buffer);
            return message;
        });
    }

    @Override
    public Optional<Message> findById(UUID id) {
        buffer = super.readFile();
        return Optional.ofNullable(buffer.get(id));
    }

    @Override
    public List<Message> findAll() {
        buffer = super.readFile();
        return new ArrayList<>(buffer.values());
    }

    @Override
    public void updateContent(UUID id, String content) {
        findById(id).ifPresent(retrieved -> {
            retrieved.updateContent(content);
            writeFile();
        });
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> {
            buffer.remove(id);
            writeFile();
        });
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        buffer.values().stream()
                .filter(message -> message.getUserId().equals(userId))
                .map(message -> message.getId())
                .forEach(toBeDeleted -> deleteById(toBeDeleted));
        writeFile();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        buffer.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(message -> message.getId())
                .forEach(toBeDeleted -> deleteById(toBeDeleted));
        writeFile();
    }

    private void writeFile() {
        super.writeFile(buffer);
    }
}
