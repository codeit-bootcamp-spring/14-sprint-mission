package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.message.entity.Message;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages = new HashMap<>();
    private final String directory;

    public FileMessageRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        Path path = Paths.get(fileDirectory);
        this.directory = fileDirectory;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException("디렉토리 생성 실패 - path: " + path, e);
        }
        messageLoad();
    }

    private String filePath() {
        return Paths.get(directory, "Message.ser").toString();
    }


    private void messageLoad() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
            new FileInputStream(filePath()))) {
            messages.putAll((Map<UUID, Message>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("기존 메시지 데이터가 없습니다. + path: " + filePath(), e);
        }
    }

    private void messageFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(filePath()))) {
            objectOutputStream.writeObject(messages);
        } catch (IOException e) {
            throw new UncheckedIOException("메시지 저장에 실패했습니다. - path: " + filePath(), e);
        }
    }

    @Override
    public Message messageAdd(Message message) {
        this.messages.put(message.getMessageId(), message);
        messageFlush();
        return message;
    }

    @Override
    public Optional<Message> findByMessage(UUID messageID) {
        return Optional.ofNullable(messages.get(messageID));
    }

    @Override
    public void delete(Message message) {
        messages.remove(message.getMessageId());
        messageFlush();
    }

    @Override
    public void update(Message message) {
        messages.replace(message.getMessageId(), message);
        messageFlush();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        messages.values().removeIf(message -> message.getChannelId().equals(channelId));
        messageFlush();
    }

    @Override
    public List<Message> findAllMessage(UUID channelId) {
        return messages.values().stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .toList();
    }
}
